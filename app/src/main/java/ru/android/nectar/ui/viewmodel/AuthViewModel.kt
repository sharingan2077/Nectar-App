package ru.android.nectar.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.android.nectar.data.model.AuthData
import ru.android.nectar.data.repository.AuthRepository
import ru.android.nectar.data.repository.AuthStorage
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authResult = MutableLiveData<Result<Unit>>()
    val authResult: LiveData<Result<Unit>> = _authResult

    private val _currentUsername = MutableLiveData<String?>()
    val currentUsername: LiveData<String?> = _currentUsername

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        _currentUsername.value = repository.getCurrentUsername()
        _isAuthenticated.value = repository.isAuthenticated()
    }

    fun register(login: String, password: String, username: String) {
        viewModelScope.launch {
            val result = repository.register(login, password, username)
            result.onSuccess {
                _currentUsername.value = username
                _isAuthenticated.value = true
            }
            _authResult.value = result
        }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(login, password)
            result.onSuccess {
                _currentUsername.value = repository.getCurrentUsername()
                _isAuthenticated.value = true
            }
            _authResult.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _currentUsername.value = null
            _isAuthenticated.value = false
        }
    }
}