package ru.android.nectar.ui.authorization

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentOnBoardingBinding


class OnBoardingFragment : Fragment() {

    private lateinit var binding : FragmentOnBoardingBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater)
        binding.btn.setOnClickListener {
//            findNavController().navigate(R.id.action_onBoardingFragment_to_signInFragment)
//            findNavController().navigate(R.id.action_onBoardingFragment_to_numberFragment)
            findNavController().navigate(R.id.action_onBoardingFragment_to_logInFragment)
        }

        return binding.root
    }


}