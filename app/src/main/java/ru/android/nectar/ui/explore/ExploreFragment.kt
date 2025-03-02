package ru.android.nectar.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.android.nectar.R
import ru.android.nectar.adapters.ProductTypeAdapter
import ru.android.nectar.databinding.FragmentExploreBinding

private const val TAG = "ExploreFragment"

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var adapter: ProductTypeAdapter
    private val exploreViewModel: ExploreViewModel by viewModels()



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

        binding.etExplore.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(R.id.action_exploreFragment_to_searchFragment)
            }
        }

        return binding.root


    }

}