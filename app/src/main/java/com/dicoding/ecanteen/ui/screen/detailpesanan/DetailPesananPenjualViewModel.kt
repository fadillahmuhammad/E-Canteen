package com.dicoding.ecanteen.ui.screen.detailpesanan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.DetailPesananPenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailPesananPenjualViewModel(private val repository: UserRepository) : ViewModel() {
    private val _detailPesananPenjualResponse = MutableStateFlow<ResultState<DetailPesananPenjualResponse>>(ResultState.Loading)
    val detailPesananPenjualResponse: StateFlow<ResultState<DetailPesananPenjualResponse>> = _detailPesananPenjualResponse

    fun getDetailPesananPenjual(idPesananPenjual: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getDetailPesananPenjual(idPesananPenjual)
            _detailPesananPenjualResponse.value = result
        }
    }
}