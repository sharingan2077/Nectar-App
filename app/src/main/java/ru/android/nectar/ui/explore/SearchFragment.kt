package ru.android.nectar.ui.explore

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.databinding.FragmentSearchBinding
import ru.android.nectar.ui.viewmodel.CartViewModel
import ru.android.nectar.ui.viewmodel.FavouriteViewModel
import ru.android.nectar.ui.viewmodel.ExploreViewModel
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import ru.android.nectar.adapters.SearchHistoryAdapter
import ru.android.nectar.ui.viewmodel.Resource

private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val exploreViewModel: ExploreViewModel by activityViewModels()
    private val favouriteViewModel: FavouriteViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter


    private lateinit var historyAdapter: SearchHistoryAdapter


    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        val query = binding.etSearch.text.toString()
        Log.d(TAG, "query is $query")
        exploreViewModel.searchProducts(query)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)

        showKeyboard(binding.etSearch)
        binding.etSearch.requestFocus()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgClose.setOnClickListener {
            binding.etSearch.text.clear()
            hideKeyboard(binding.etSearch)
        }

        setupRecyclerView()
        observeData()
        setupRetryButton()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchDebounce()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imgClose.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

                if (s.isNullOrEmpty()) {
                    if (binding.etSearch.hasFocus()) {
                        showInitialState()
                    }
                }
            }
        })

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_SEARCH) {
                hideKeyboard(v)
                executeSearch(binding.etSearch.text.toString())
                true
            } else {
                false
            }
        }


        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                exploreViewModel.loadSearchHistory()
            }
        }

        binding.imgClearHistory.setOnClickListener {
            exploreViewModel.clearSearchHistory()
            showHistory(emptyList())
        }

        exploreViewModel.loadSearchHistory()
        showInitialState()

        return binding.root
    }

    private fun setupRetryButton() {
        binding.retryButton.setOnClickListener {
            // Повторяем последний поисковый запрос
            val lastQuery = binding.etSearch.text.toString()
            if (lastQuery.isNotEmpty()) {
                executeSearch(lastQuery)
            }
        }
    }

    private fun executeSearch(query: String) {
        Log.d(TAG, "Execute Search")
        showLoading()
        exploreViewModel.searchProducts(query)
    }

    private fun showHistory(history: List<String>) {
        if (history.isNotEmpty()) {
            Log.d(TAG, "Showing History")
            binding.rvSearchHistory.visibility = View.VISIBLE
            binding.tvHistoryTitle.visibility = View.VISIBLE
            binding.imgClearHistory.visibility = View.VISIBLE
            historyAdapter.submitList(history)
        } else {
            Log.d(TAG, "Hiding History")
            binding.rvSearchHistory.visibility = View.GONE
            binding.tvHistoryTitle.visibility = View.GONE
            binding.imgClearHistory.visibility = View.GONE
        }
    }

    private fun showInitialState() {
//        Log.d(TAG, "Showing Initial State")
        binding.pbFindProducts.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        binding.emptyResultsPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
        binding.initialPlaceholder.visibility = View.VISIBLE

    }

    private fun showLoading() {
        Log.d(TAG, "Showing Loading")
        binding.pbFindProducts.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
        binding.emptyResultsPlaceholder.visibility = View.GONE
        binding.initialPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
//        binding.rvSearchHistory.visibility = View.GONE
//        binding.tvHistoryTitle.visibility = View.GONE
//        binding.imgClearHistory.visibility = View.GONE
    }

    private fun showResults() {
        binding.pbFindProducts.visibility = View.GONE
        binding.rvSearch.visibility = View.VISIBLE
        binding.emptyResultsPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
        binding.initialPlaceholder.visibility = View.GONE
    }

    private fun showEmptyResults() {
        binding.pbFindProducts.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        binding.emptyResultsPlaceholder.visibility = View.VISIBLE
        binding.errorPlaceholder.visibility = View.GONE
        binding.initialPlaceholder.visibility = View.GONE
    }

    private fun showError() {
        binding.pbFindProducts.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        binding.emptyResultsPlaceholder.visibility = View.GONE
        binding.initialPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {

        historyAdapter = SearchHistoryAdapter { query ->
            binding.etSearch.setText(query)
            binding.etSearch.setSelection(query.length)
            executeSearch(query)
            hideKeyboard(binding.etSearch)
        }
        binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchHistory.adapter = historyAdapter


        adapter = ProductAdapter(
            onCartCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.isCart(product.id).collect { isCart ->
                        callback(isCart)
                    }
                }
            },
            onCartClick = { product ->
                cartViewModel.addCart(product.id)
            },
            onFavoriteCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    favouriteViewModel.isFavorite(product.id).collect { isFav ->
                        callback(isFav)
                    }
                }
            },
            onFavoriteClick = { product ->
                favouriteViewModel.toggleFavorite(product.id)
            },
            onProductClick = { product ->
                exploreViewModel.addProductToHistory(product)
            }
        )

        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@SearchFragment.adapter
        }

    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            exploreViewModel.searchHistory.collectLatest { history ->
                if (binding.etSearch.hasFocus() && history.isNotEmpty()) {
                    Log.d(TAG, "Show History")
                    showHistory(history.map { it.name })
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            exploreViewModel.searchResults.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "Is Resource Loading")
                        showLoading()
                    }
                    is Resource.Success -> {
                        if (result.data.isNullOrEmpty()) {
                            if (binding.etSearch.text.isNullOrEmpty()) {
                                showInitialState()
                            } else {
                                showEmptyResults()
                            }
                        } else {
                            adapter.submitList(result.data)
                            showResults()
                        }
                    }

                    is Resource.Error -> {
                        showError()
                    }
                }
            }
        }
    }


    private fun showKeyboard(view: View) {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }


    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        val query = binding.etSearch.text.toString()

        when {
            query.isEmpty() -> {
                if (binding.etSearch.hasFocus()) {
                    showInitialState()
                }
                return
            }

            else -> {
                showLoading()
                handler.postDelayed(searchRunnable, 500L)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(searchRunnable)
    }

}