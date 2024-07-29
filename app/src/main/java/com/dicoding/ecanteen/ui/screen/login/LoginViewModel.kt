package com.dicoding.ecanteen.ui.screen.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.local.pref.UserModel
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<ResultState<UserModel>>(ResultState.Loading)
    val loginResult: StateFlow<ResultState<UserModel>> = _loginResult

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    fun loginPembeli(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            try {
                val result = repository.loginPembeli(email, password)
                _loginResult.value = result
            } catch (e: Exception) {
                _loginResult.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun loginPenjual(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            try {
                val result = repository.loginPenjual(email, password)
                _loginResult.value = result
            } catch (e: Exception) {
                _loginResult.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun saveSessionPembeli(user: UserModel) {
        viewModelScope.launch {
            repository.saveSessionPembeli(user)
        }
    }

    fun saveSessionPenjual(user: UserModel) {
        viewModelScope.launch {
            repository.saveSessionPenjual(user)
        }
    }
}