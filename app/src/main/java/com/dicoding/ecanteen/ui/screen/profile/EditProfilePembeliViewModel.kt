package com.dicoding.ecanteen.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.EditProfilePembeliResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfilePembeliViewModel(private val repository: UserRepository) : ViewModel() {
    private val _editProfilePembeliState = MutableStateFlow<ResultState<EditProfilePembeliResponse>>(ResultState.Loading)
    val editProfilePembeliState: StateFlow<ResultState<EditProfilePembeliResponse>> = _editProfilePembeliState

    fun editProfilePembeli(
        id: Int,
        fullName: String,
        telp: String,
    ) {
        viewModelScope.launch {
            _editProfilePembeliState.value = ResultState.Loading
            val result = repository.editProfilePembeli(id, fullName, telp)
            _editProfilePembeliState.value = result
        }
    }
}