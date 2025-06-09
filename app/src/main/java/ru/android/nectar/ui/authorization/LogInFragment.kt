package ru.android.nectar.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentLogInBinding
import ru.android.nectar.ui.viewmodel.AuthViewModel


@AndroidEntryPoint
class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels() // Или activityViewModels(), если ViewModel общая

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogIn.setOnClickListener {
            val login = binding.etLogin.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (login.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(login, password)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.llSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        authViewModel.authResult.observe(viewLifecycleOwner) { result ->
            result?.fold(
                onSuccess = { token ->
                    Toast.makeText(requireContext(), "Logged in!", Toast.LENGTH_SHORT).show()
                    // Навигация в главный экран, например:
                    findNavController().navigate(R.id.action_logInFragment_to_shopFragment)
                },
                onFailure = { e ->
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
