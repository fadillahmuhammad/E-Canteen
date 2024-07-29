package com.dicoding.ecanteen.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.local.pref.UserModel
import com.dicoding.ecanteen.data.remote.response.GetProfilePembeliResponse
import com.dicoding.ecanteen.data.remote.response.GetProfilePenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userModel = MutableStateFlow<UserModel?>(null)
    val userModel: StateFlow<UserModel?> get() = _userModel

    fun getUserSessionPenjual() {
        viewModelScope.launch {
            repository.getUserSessionPenjual()?.let {
                _userModel.value = it
            }
        }
    }

    fun getUserSessionPembeli() {
        viewModelScope.launch {
            repository.getUserSessionPembeli()?.let {
                _userModel.value = it
            }
        }
    }

    private val _getProfilePembeliState = MutableStateFlow<ResultState<GetProfilePembeliResponse>>(
        ResultState.Loading)
    val getProfilePembeliState: StateFlow<ResultState<GetProfilePembeliResponse>> = _getProfilePembeliState

    fun getProfilePembeli(
        id: Int,
    ) {
        viewModelScope.launch {
            _getProfilePembeliState.value = ResultState.Loading
            val result = repository.getProfilePembeli(id)
            _getProfilePembeliState.value = result
        }
    }

    private val _getProfilePenjualState = MutableStateFlow<ResultState<GetProfilePenjualResponse>>(
        ResultState.Loading)
    val getProfilePenjualState: StateFlow<ResultState<GetProfilePenjualResponse>> = _getProfilePenjualState

    fun getProfilePenjual(
        id: Int,
    ) {
        viewModelScope.launch {
            _getProfilePenjualState.value = ResultState.Loading
            val result = repository.getProfilePenjual(id)
            _getProfilePenjualState.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun clearUserModel() {
        _userModel.value = null
    }
}