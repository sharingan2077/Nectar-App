package ru.android.nectar.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.nectar.data.repository.AuthRepository
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpStatus = MutableLiveData<Boolean>()
    val signUpStatus: LiveData<Boolean> = _signUpStatus

    private val _signInStatus = MutableLiveData<Boolean>()
    val signInStatus: LiveData<Boolean> = _signInStatus

    fun signUpWithEmail(email: String, password: String) {
        authRepository.signUpWithEmail(email, password) { success, exception ->
            _signUpStatus.value = success
            if (!success) {
                Log.e("AuthViewModel", "Sign-up failed: ${exception?.message}")
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        authRepository.signInWithEmail(email, password) { success, exception ->
            _signInStatus.value = success
            if (!success) {
                Log.e("AuthViewModel", "Sign-in failed: ${exception?.message}")
            }
        }
    }


    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _verificationStatus = MutableLiveData<Boolean>()
    val verificationStatus: LiveData<Boolean> = _verificationStatus

    fun getCurrentUser() {
        _currentUser.value = authRepository.getCurrentUser()
    }

    fun verifyPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        callbacks: OnVerificationStateChangedCallbacks
    ) {
        authRepository.verifyPhoneNumber(phoneNumber, activity, callbacks)
    }

    fun signInWithCredential(credential: PhoneAuthCredential) {
        authRepository.signInWithCredential(credential) { success, exception ->
            _signInStatus.value = success
            if (!success) {
                Log.e("AuthViewModel", "Sign-in failed: ${exception?.message}")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}

