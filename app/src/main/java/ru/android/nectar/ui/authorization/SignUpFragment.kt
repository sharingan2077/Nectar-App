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
import ru.android.nectar.databinding.FragmentSignUpBinding
import ru.android.nectar.ui.viewmodel.AuthViewModel


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signUpWithEmail(email, password)
            }
        }
        binding.llLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }
        authViewModel.signUpStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
            }
            else {
                Toast.makeText(requireContext(), "Failed to Sign Up", Toast.LENGTH_LONG).show()
            }
        }
    }
}