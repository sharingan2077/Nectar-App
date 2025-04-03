package ru.android.nectar.ui.explore

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import ru.android.nectar.adapters.SearchProductAdapter
import ru.android.nectar.databinding.FragmentSearchBinding
import ru.android.nectar.databinding.LayoutErrorBinding
import ru.android.nectar.databinding.LayoutNoResultsBinding
import ru.android.nectar.ui.viewmodel.CartViewModel
import ru.android.nectar.ui.viewmodel.FavouriteViewModel
import ru.android.nectar.ui.viewmodel.ExploreViewModel
import ru.android.nectar.ui.viewmodel.SearchViewModel
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import ru.android.nectar.adapters.SearchHistoryAdapter

private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val exploreViewModel: ExploreViewModel by activityViewModels()
    private val favouriteViewModel: FavouriteViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter

    private lateinit var searchProductAdapter: SearchProductAdapter
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var bindingLayoutOne: LayoutNoResultsBinding
    private lateinit var bindingLayoutTwo: LayoutErrorBinding

    private lateinit var historyAdapter: SearchHistoryAdapter



    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        val query = binding.etSearch.text.toString()
        exploreViewModel.searchProducts(query)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)

        bindingLayoutOne = LayoutNoResultsBinding.inflate(inflater)
        bindingLayoutTwo = LayoutErrorBinding.inflate(inflater)

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

        // Логика поиска (добавить адаптер и фильтрацию)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchDebounce()
//                exploreViewModel.searchProducts(s.toString())
//                searchViewModel.searchProducts(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imgClose.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_SEARCH) {
                hideKeyboard(v)
                exploreViewModel.searchProducts(binding.etSearch.text.toString())
                true
            } else {
                false
            }
        }
//        binding.rvSearch.visibility = View.GONE
//        bindingLayoutOne.llNoResults.visibility = View.VISIBLE
//        bindingLayoutTwo.llError.visibility = View.GONE


//        bindingLayoutTwo.btnRetry.setOnClickListener {
//            searchViewModel.retryLastSearch()
//        }

        // Инициализация адаптера
        historyAdapter = SearchHistoryAdapter { query ->
            binding.etSearch.setText(query)
            exploreViewModel.searchProducts(query)
            hideKeyboard(binding.etSearch)
        }

        binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchHistory.adapter = historyAdapter

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && getHistory().isNotEmpty()) {
                showHistory()
            }
        }

        binding.imgClearHistory.setOnClickListener {
            clearHistory()
            showHistory()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onCartCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.isCart(1, product.id).collect { isCart ->
                        callback(isCart)
                    }
                }
            },
            onCartClick = { product ->
                cartViewModel.addCart(1, product.id)
            },
            onFavoriteCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    favouriteViewModel.isFavorite(1, product.id).collect { isFav ->
                        callback(isFav)
                    }
                }
            },
            onFavoriteClick = { product ->
                favouriteViewModel.toggleFavorite(1, product.id)
            },
            onProductClick = { product ->
                saveToHistory(product.name)
            }
        )

        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@SearchFragment.adapter
        }


//        searchProductAdapter = SearchProductAdapter()
//        binding.rvSearch.apply {
//            layoutManager = GridLayoutManager(requireContext(), 2)
//            adapter = this@SearchFragment.searchProductAdapter
//        }



    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            exploreViewModel.searchResults.collectLatest { products ->
                adapter.submitList(products)

                binding.progressBar.visibility = View.GONE

                if (products.isNotEmpty()) {
                    binding.rvSearch.visibility = View.VISIBLE
                } else {
                    binding.rvSearch.visibility = View.GONE
                    // Можно тут показать пустой плейсхолдер, если нужно
                }

            }
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            searchViewModel.searchResults.collectLatest { products ->
//                searchProductAdapter.updateData(products)
//            }
//        }
//        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
//            searchViewModel.searchState.collectLatest { state ->
//                when (state) {
//                    is SearchState.Loading -> {
//                        binding.rvSearch.visibility = View.GONE
//                        bindingLayoutOne.llNoResults.visibility = View.GONE
//                        bindingLayoutTwo.llError.visibility = View.GONE
//                    }
//                    is SearchState.Success -> {
//                        binding.rvSearch.visibility = View.VISIBLE
//                        bindingLayoutOne.llNoResults.visibility = View.GONE
//                        bindingLayoutTwo.llError.visibility = View.GONE
//                        searchProductAdapter.updateData(state.products)
//                    }
//                    is SearchState.Empty -> {
//                        binding.rvSearch.visibility = View.GONE
//                        bindingLayoutOne.llNoResults.visibility = View.VISIBLE
//                        bindingLayoutTwo.llError.visibility = View.GONE
//                    }
//                    is SearchState.Error -> {
//                        binding.rvSearch.visibility = View.GONE
//                        bindingLayoutOne.llNoResults.visibility = View.GONE
//                        bindingLayoutTwo.llError.visibility = View.VISIBLE
//                    }
//                }
//            }
//        }
    }


    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }


    private fun getHistory(): List<String> {
        val prefs = requireContext().getSharedPreferences("search_history", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("history", emptySet()) ?: emptySet()
        return set.toMutableList().sortedByDescending { it } // Последние сверху
    }

    private fun saveToHistory(query: String) {
        if (query.isBlank()) return
        val prefs = requireContext().getSharedPreferences("search_history", Context.MODE_PRIVATE)
        val history = getHistory().toMutableList()
        history.remove(query) // убираем, чтобы не было дубликатов
        history.add(0, query) // добавляем в начало
        if (history.size > 10) {
            history.removeAt(history.lastIndex)
        }
        prefs.edit() { putStringSet("history", history.toSet()) }
    }

    private fun clearHistory() {
        val prefs = requireContext().getSharedPreferences("search_history", Context.MODE_PRIVATE)
        prefs.edit() { clear() }
    }
    private fun showHistory() {
        val history = getHistory()
        if (history.isNotEmpty()) {
            binding.rvSearchHistory.visibility = View.VISIBLE
            binding.tvHistoryTitle.visibility = View.VISIBLE
            binding.imgClearHistory.visibility = View.VISIBLE
            historyAdapter.submitList(history)
        } else {
            binding.rvSearchHistory.visibility = View.GONE
            binding.tvHistoryTitle.visibility = View.GONE
            binding.imgClearHistory.visibility = View.GONE
        }
    }

//    private fun searchDebounce() {
//        handler.removeCallbacks(searchRunnable)
//        handler.postDelayed(searchRunnable, 2000L) // 2 секунды
//    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)

        // Показываем прогресс, скрываем всё остальное
        binding.progressBar.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
        binding.rvSearchHistory.visibility = View.GONE
        binding.tvHistoryTitle.visibility = View.GONE
        binding.imgClearHistory.visibility = View.GONE

        handler.postDelayed(searchRunnable, 2000L)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(searchRunnable)
    }





}