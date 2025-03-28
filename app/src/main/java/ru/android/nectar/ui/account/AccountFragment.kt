package ru.android.nectar.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentAccountBinding
import ru.android.nectar.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    private val authViewModel: AuthViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)

        binding.btnLogout.setOnClickListener {
            authViewModel.signOut()
            findNavController().navigate(R.id.action_accountFragment_to_onBoardingFragment)
        }

        return binding.root
    }


}