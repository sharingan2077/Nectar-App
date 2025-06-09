package ru.android.nectar.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentSplashScreenBinding
import ru.android.nectar.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.isAuthenticated.observe(viewLifecycleOwner) { isAuth ->
            if (isAuth) {
                // Пользователь уже авторизован — идём в магазин
                findNavController().navigate(R.id.action_splashScreenFragment_to_shopFragment)
            } else {
                // Пользователь не авторизован — идём в onboarding/login
                findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
            }
        }

    }
}
