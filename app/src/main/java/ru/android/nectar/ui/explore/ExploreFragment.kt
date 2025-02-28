package ru.android.nectar.ui.explore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.adapters.ProductTypeAdapter
import ru.android.nectar.databinding.FragmentExploreBinding
import ru.android.nectar.ui.favourite.FavouriteViewModel
import ru.android.nectar.ui.shop.ShopViewModel

private const val TAG = "ExploreFragment"

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var adapter: ProductTypeAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater)

        adapter = ProductTypeAdapter(dataProductTypesList)
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@ExploreFragment.adapter
        }

        return binding.root


    }

}