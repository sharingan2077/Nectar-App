package ru.android.nectar.ui.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.android.nectar.R
import ru.android.nectar.databinding.FragmentNumberBinding
import ru.android.nectar.ui.viewmodel.AuthViewModel

//@AndroidEntryPoint
//class NumberFragment : Fragment() {
//
//    private lateinit var binding: FragmentNumberBinding
//    private val authViewModel: AuthViewModel by activityViewModels()
//    private lateinit var storedVerificationId: String
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentNumberBinding.inflate(inflater)
//
//        binding.btnNext.setOnClickListener {
//            val phoneNumber = binding.etPhone.text.toString().trim()
//            if (phoneNumber.isNotEmpty()) {
//                authViewModel.verifyPhoneNumber(phoneNumber, requireActivity(),
//                    object : OnVerificationStateChangedCallbacks() {
//                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                            authViewModel.signInWithCredential(credential)
//                        }
//                        override fun onVerificationFailed(e: FirebaseException) {
//
//                        }
//                        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken
//                        ) {
//                            storedVerificationId = verificationId
//
//                        }
//
//                    })
//
//            }
//        }
//
//        lifecycleScope.launch {
//            authViewModel.verificationResult.collect { result ->
//                result?.onSuccess {
//                    val action = NumberFragmentDirections.actionNumberFragmentToVerificationFragment(verificationId = storedVerificationId)
//                    findNavController().navigate(action)
//                }?.onFailure {
//                    Toast.makeText(requireContext(), "Failure to send code", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//
//        return binding.root
//    }
//}
private const val TAG = "NumberFragment"

class NumberFragment : Fragment() {
    private lateinit var binding: FragmentNumberBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var storedVerificationId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberBinding.inflate(inflater)

//        binding.btnNext.setOnClickListener {
//            val phoneNumber = binding.etPhone.text.toString().trim()
//            if (phoneNumber.isNotEmpty()) {
//                authViewModel.verifyPhoneNumber(phoneNumber, requireActivity(),
//                    object : OnVerificationStateChangedCallbacks() {
//                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                            authViewModel.signInWithCredential(credential)
//                        }
//
//                        override fun onVerificationFailed(e: FirebaseException) {
//                            Log.w(TAG, "onVerificationFailed", e)
//                            if (e is FirebaseAuthInvalidCredentialsException) {
//                                Log.w(TAG, "onVerificationFailed", e)
//                            } else if (e is FirebaseTooManyRequestsException) {
//                                Log.w(TAG, "onVerificationFailed", e)
//                            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                                Log.w(TAG, "onVerificationFailed", e)
//                            }
//                            Toast.makeText(requireContext(), "Не удалось прислать код", Toast.LENGTH_LONG).show()
//                        }
//
//                        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//                            storedVerificationId = verificationId
//                            val action = NumberFragmentDirections.actionNumberFragmentToVerificationFragment(verificationId)
//                            findNavController().navigate(action)
//                        }
//                    })
//            }
//        }
//
//        authViewModel.signInStatus.observe(viewLifecycleOwner) { success ->
//            if (success) {
//                findNavController().navigate(R.id.action_numberFragment_to_shopFragment)
//            } else {
//                Toast.makeText(requireContext(), "Ошибка входа", Toast.LENGTH_LONG).show()
//            }
//        }

        return binding.root
    }
}

