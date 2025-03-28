package ru.android.nectar.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentLogInBinding
import ru.android.nectar.ui.viewmodel.AuthViewModel


class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signInWithEmail(email, password)
            }
        }
        binding.llSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
        authViewModel.signInStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
//                findNavController().navigate(R.id.shopFragment) {
//                    popUpTo(R.id.logInFragment) { inclusive = true }
//                }
                findNavController().navigate(R.id.action_logInFragment_to_shopFragment)
            }
            else {
                Toast.makeText(requireContext(), "Failed to Log In", Toast.LENGTH_LONG).show()
            }
        }
    }
}