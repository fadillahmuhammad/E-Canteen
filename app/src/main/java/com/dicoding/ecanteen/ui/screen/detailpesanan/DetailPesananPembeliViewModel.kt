package com.dicoding.ecanteen.ui.screen.detailpesanan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.DetailPesananPembeliResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailPesananPembeliViewModel(private val repository: UserRepository) : ViewModel() {
    private val _detailPesananPembeliResponse = MutableStateFlow<ResultState<DetailPesananPembeliResponse>>(ResultState.Loading)
    val detailPesananPembeliResponse: StateFlow<ResultState<DetailPesananPembeliResponse>> = _detailPesananPembeliResponse

    fun getDetailPesananPembeli(idPesananPembeli: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getDetailPesananPembeli(idPesananPembeli)
            _detailPesananPembeliResponse.value = result
        }
    }
}