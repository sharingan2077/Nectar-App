package ru.android.nectar.ui.authorization

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater)
        // Используем Handler с основным потоком (UI поток)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
//            findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
            findNavController().navigate(R.id.action_splashScreenFragment_to_shopFragment)
        }, 1) // Задержка в 3 секунды
        return binding.root

}

}