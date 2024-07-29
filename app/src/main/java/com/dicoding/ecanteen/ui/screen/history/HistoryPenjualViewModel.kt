package com.dicoding.ecanteen.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.HistoryPenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryPenjualViewModel(private val repository: UserRepository) : ViewModel() {
    private val _historyPenjualResponse = MutableStateFlow<ResultState<HistoryPenjualResponse>>(
        ResultState.Loading)
    val historyPenjualResponse: StateFlow<ResultState<HistoryPenjualResponse>> = _historyPenjualResponse

    fun getHistoryPenjual(idPenjual: Int, year: Int, month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getHistoryPenjual(idPenjual, year, month)
            _historyPenjualResponse.value = result
        }
    }
}