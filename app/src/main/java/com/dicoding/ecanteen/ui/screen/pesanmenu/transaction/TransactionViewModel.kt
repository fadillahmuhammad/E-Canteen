package com.dicoding.ecanteen.ui.screen.pesanmenu.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.PesanMenuResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: UserRepository) : ViewModel() {
    private val _pesanMenuState = MutableStateFlow<ResultState<PesanMenuResponse>>(
        ResultState.Loading)
    val pesanMenuState: StateFlow<ResultState<PesanMenuResponse>> = _pesanMenuState

    fun pesanMenu(
        idPembeli: Int,
        idPenjual: Int,
        idMenu: Int,
        noPesanan: String,
        qty: Int,
        catatan: String,
        jamPesan: String,
        jmlHarga: Int,
        status: Boolean,
    ) {
        viewModelScope.launch {
            _pesanMenuState.value = ResultState.Loading
            val result = repository.pesanMenu(idPembeli, idPenjual, idMenu, noPesanan, qty, catatan, jamPesan, jmlHarga, status)
            _pesanMenuState.value = result
        }
    }
}
