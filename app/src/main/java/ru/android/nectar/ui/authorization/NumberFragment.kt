package ru.android.nectar.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentNumberBinding


class NumberFragment : Fragment() {

    private lateinit var binding: FragmentNumberBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberBinding.inflate(inflater)
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_numberFragment_to_verificationFragment)
        }
        return binding.root
    }


}