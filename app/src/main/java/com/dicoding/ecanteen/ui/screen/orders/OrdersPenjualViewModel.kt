package com.dicoding.ecanteen.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.PesananPenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersPenjualViewModel(private val repository: UserRepository) : ViewModel() {
    private val _pesananPenjualResponse = MutableStateFlow<ResultState<PesananPenjualResponse>>(
        ResultState.Loading)
    val pesananPenjualResponse: StateFlow<ResultState<PesananPenjualResponse>> = _pesananPenjualResponse

    fun getPesananPenjual(idPenjual: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getPesananPenjual(idPenjual)
            _pesananPenjualResponse.value = result
        }
    }

    private val _editStatusResponse = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
    val editStatusResponse: StateFlow<ResultState<Unit>> = _editStatusResponse

    fun editStatusPesanan(idPenjual: Int, id: Int, statusBool: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.editStatusPesanan(id, statusBool)
            _editStatusResponse.value = result
            if (result is ResultState.Success) {
                getPesananPenjual(idPenjual)
            }
        }
    }
}
