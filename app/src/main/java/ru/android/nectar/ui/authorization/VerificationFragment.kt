package ru.android.nectar.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentVerificationBinding
import ru.android.nectar.ui.viewmodel.AuthViewModel

//@AndroidEntryPoint
//class VerificationFragment : Fragment() {
//
//    private lateinit var binding: FragmentVerificationBinding
//    private val authViewModel: AuthViewModel by activityViewModels()
//
//    private lateinit var verificationId: String
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentVerificationBinding.inflate(inflater)
//
//        arguments?.let {
//            verificationId = VerificationFragmentArgs.fromBundle(it).verificationId
//        }
//
//        binding.btnNext.setOnClickListener {
//            val code = binding.etVerification.text.toString().trim()
//            if (code.isNotEmpty()) {
//                val credential = PhoneAuthProvider.getCredential(verificationId, code)
//                authViewModel.signInWithCredential(credential)
//            }
//        }
//
//        lifecycleScope.launch {
//            authViewModel.signInResult.collect { result ->
//                result?.onSuccess {
//                    // Переход в ShopFragment после успешной верификации
//                    findNavController().navigate(R.id.action_verificationFragment_to_shopFragment)
//                }?.onFailure {
//                    // Обрабатываем ошибку
//                }
//            }
//        }
//
//        return binding.root
//    }
//}
class VerificationFragment : Fragment() {
    private lateinit var binding: FragmentVerificationBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var verificationId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationBinding.inflate(inflater)

//        arguments?.let {
//            verificationId = VerificationFragmentArgs.fromBundle(it).verificationId
//        }
//
//        binding.btnNext.setOnClickListener {
//            val code = binding.etVerification.text.toString().trim()
//            if (code.isNotEmpty()) {
//                val credential = PhoneAuthProvider.getCredential(verificationId, code)
//                authViewModel.signInWithCredential(credential)
//            }
//        }
//
//        authViewModel.signInStatus.observe(viewLifecycleOwner) { success ->
//            if (success) {
//                findNavController().navigate(R.id.action_verificationFragment_to_shopFragment)
//            } else {
//                Toast.makeText(requireContext(), "Ошибка входа", Toast.LENGTH_LONG).show()
//            }
//        }

        return binding.root
    }
}
