package com.dicoding.ecanteen.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.HistoryPembeliResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryPembeliViewModel(private val repository: UserRepository) : ViewModel() {
    private val _historyPembeliResponse = MutableStateFlow<ResultState<HistoryPembeliResponse>>(
        ResultState.Loading)
    val historyPembeliResponse: StateFlow<ResultState<HistoryPembeliResponse>> = _historyPembeliResponse

    fun getHistoryPembeli(idPembeli: Int, year: Int, month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getHistoryPembeli(idPembeli, year, month)
            _historyPembeliResponse.value = result
        }
    }
}