
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.SaldoResponse
import com.dicoding.ecanteen.data.remote.response.TransaksiSaldoResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EwalletPembeliViewModel(private val repository: UserRepository) : ViewModel() {

    private val _saldoResponse = MutableStateFlow<ResultState<SaldoResponse>>(ResultState.Loading)
    val saldoResponse: StateFlow<ResultState<SaldoResponse>> = _saldoResponse

    private val _transaksiSaldoResponse = MutableStateFlow<ResultState<TransaksiSaldoResponse>>(ResultState.Loading)
    val transaksiSaldoResponse: StateFlow<ResultState<TransaksiSaldoResponse>> = _transaksiSaldoResponse

    fun getSaldo(idUser: Int, tipeUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getSaldo(idUser, tipeUser)
            _saldoResponse.value = result
        }
    }

    fun getTransaksiSaldo(idUser: Int, tipeUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getTransaksiSaldo(idUser, tipeUser)
            _transaksiSaldoResponse.value = result
        }
    }
}
