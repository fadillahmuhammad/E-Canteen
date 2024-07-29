package com.dicoding.ecanteen.ui.screen.topup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.TopUpResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TopUpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _selectedAmount = mutableStateOf("")
    val selectedAmount: State<String> = _selectedAmount

    private val _topUpStatus = mutableStateOf<ResultState<TopUpResponse>>(ResultState.Loading)
    val topUpStatus: State<ResultState<TopUpResponse>> = _topUpStatus

    fun setAmount(value: String) {
        _amount.value = value
    }

    fun setSelectedAmount(amount: String) {
        _selectedAmount.value = amount
    }

    fun topUpSaldo(idUser: Int, tipeUser: String, idSaldo: Int, amount: Int) {
        viewModelScope.launch {
            _topUpStatus.value = ResultState.Loading
            val result = repository.topUpSaldo(idUser, tipeUser, idSaldo, amount)
            _topUpStatus.value = result
        }
    }
}
