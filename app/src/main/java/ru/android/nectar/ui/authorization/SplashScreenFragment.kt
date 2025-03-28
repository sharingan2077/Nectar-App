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
        binding = FragmentSplashScreenBinding.inflate(inflater)
        // Используем Handler с основным потоком (UI поток)

        authViewModel.getCurrentUser() // Получаем текущего пользователя

//        findNavController().navigate(R.id.action_splashScreenFragment_to_shopFragment)


        lifecycleScope.launch {
            authViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    // Если пользователь авторизован, переходим в ShopFragment
                    findNavController().navigate(R.id.action_splashScreenFragment_to_shopFragment)
                } else {
                    // Если пользователь не авторизован, переходим в NumberFragment
                    findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                }
            }
        }
        return binding.root

}

}