package com.dicoding.ecanteen.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.PesananPembeliResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersPembeliViewModel(private val repository: UserRepository) : ViewModel() {
    private val _pesananPembeliResponse = MutableStateFlow<ResultState<PesananPembeliResponse>>(
        ResultState.Loading)
    val pesananPembeliResponse: StateFlow<ResultState<PesananPembeliResponse>> = _pesananPembeliResponse

    fun getPesananPembeli(idPembeli: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getPesananPembeli(idPembeli)
            _pesananPembeliResponse.value = result
        }
    }

    // fungsi hapus
    fun deletePesananFromId(idPembeli: Int, idPesanan: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.deletePesananFromId(idPesanan)) {
                is ResultState.Success -> {
                    getPesananPembeli(idPembeli) // Refresh the list after deletion
                }
                is ResultState.Error -> {
                    _pesananPembeliResponse.value = result
                }

                else -> {}
            }
        }
    }
}
