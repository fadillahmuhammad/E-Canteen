package com.dicoding.ecanteen.ui.screen.register

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

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResult = MutableStateFlow<ResultState<UserModel>>(ResultState.Loading)
    val registerResult: StateFlow<ResultState<UserModel>> = _registerResult

    private val _storename = mutableStateOf("")
    val storename: State<String> = _storename

    private val _fullname = mutableStateOf("")
    val fullname: State<String> = _fullname

    private val _telp = mutableStateOf("")
    val telp: State<String> = _telp

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setStoreName(storename: String) {
        _storename.value = storename
    }

    fun setFullName(fullname: String) {
        _fullname.value = fullname
    }

    fun setTelp(telp: String) {
        _telp.value = telp
    }

    fun registerPembeli(
        fullName: String,
        telp: String,
        idKartu: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            try {
                val result = repository.registerPembeli(fullName, telp, idKartu, email, password)
                _registerResult.value = result
            } catch (e: Exception) {
                _registerResult.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun registerPenjual(
        storeName: String,
        fullName: String,
        telp: String,
        idKartu: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            try {
                val result = repository.registerPenjual(storeName, fullName, telp, idKartu, email, password)
                _registerResult.value = result
            } catch (e: Exception) {
                _registerResult.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
