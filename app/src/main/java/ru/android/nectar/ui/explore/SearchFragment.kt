package ru.android.nectar.ui.explore

import android.content.Context
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.databinding.FragmentSearchBinding
import ru.android.nectar.ui.cart.CartViewModel
import ru.android.nectar.ui.favourite.FavouriteViewModel

private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val exploreViewModel: ExploreViewModel by activityViewModels()
    private val favouriteViewModel: FavouriteViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter

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

        // Логика поиска (добавить адаптер и фильтрацию)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                exploreViewModel.searchProducts(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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
            }
        )

        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@SearchFragment.adapter
        }

    }

    private fun observeData() {
//        viewModel.loadFavouriteProducts(1)
        viewLifecycleOwner.lifecycleScope.launch {
            exploreViewModel.searchResults.collectLatest { products ->
                adapter.submitList(products)

            }
        }
    }


    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

}