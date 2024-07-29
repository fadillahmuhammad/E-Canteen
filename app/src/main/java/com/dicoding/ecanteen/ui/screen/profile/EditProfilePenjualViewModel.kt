package com.dicoding.ecanteen.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.EditProfilePenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfilePenjualViewModel(private val repository: UserRepository) : ViewModel() {
    private val _editProfilePenjualState = MutableStateFlow<ResultState<EditProfilePenjualResponse>>(ResultState.Loading)
    val editProfilePenjualState: StateFlow<ResultState<EditProfilePenjualResponse>> = _editProfilePenjualState

    fun editProfilePenjual(
        id: Int,
        fullName: String,
        telp: String,
        namaToko: String,
        deskripsi: String,
    ) {
        viewModelScope.launch {
            _editProfilePenjualState.value = ResultState.Loading
            val result = repository.editProfilePenjual(id, fullName, telp, namaToko, deskripsi)
            _editProfilePenjualState.value = result
        }
    }
}