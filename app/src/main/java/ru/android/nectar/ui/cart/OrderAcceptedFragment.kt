package ru.android.nectar.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.nectar.databinding.FragmentOrderAcceptedBinding


class OrderAcceptedFragment : Fragment() {

    private lateinit var binding: FragmentOrderAcceptedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderAcceptedBinding.inflate(inflater)
        return binding.root
    }


}