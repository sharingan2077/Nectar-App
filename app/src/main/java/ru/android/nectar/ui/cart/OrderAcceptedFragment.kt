package ru.android.nectar.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentOrderAcceptedBinding


class OrderAcceptedFragment : Fragment() {

    private lateinit var binding: FragmentOrderAcceptedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderAcceptedBinding.inflate(inflater)
        binding.tvReturnToHome.setOnClickListener {
            findNavController().navigate(R.id.action_orderAcceptedFragment_to_shopFragment)
        }
        return binding.root
    }


}