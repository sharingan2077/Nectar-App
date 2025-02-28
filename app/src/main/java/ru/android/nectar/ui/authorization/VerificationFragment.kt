package ru.android.nectar.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentVerificationBinding


class VerificationFragment : Fragment() {

    private lateinit var binding: FragmentVerificationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationBinding.inflate(inflater)
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_verificationFragment_to_shopFragment)
        }
        return binding.root
    }


}