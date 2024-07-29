package com.dicoding.ecanteen.ui.screen.crudmenu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.local.pref.MenuAddModel
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AddMenuViewModel(private val repository: UserRepository) : ViewModel() {
    private val _addMenuResult = MutableStateFlow<ResultState<MenuAddModel>>(ResultState.Loading)
    val addMenuResult: StateFlow<ResultState<MenuAddModel>> = _addMenuResult

    private val _menuname = mutableStateOf("")
    val menuname: State<String> = _menuname

    private val _menuprice = mutableStateOf("")
    val menuprice: State<String> = _menuprice

    private val _menustok = mutableStateOf("")
    val menustok: State<String> = _menustok

    private val _menudesc = mutableStateOf("")
    val menudesc: State<String> = _menudesc

    fun setMenuName(menuName: String) {
        _menuname.value = menuName
    }

    fun setMenuPrice(menuPrice: String) {
        _menuprice.value = menuPrice
    }

    fun setMenuStok(menuStok: String) {
        _menustok.value = menuStok
    }

    fun setMenuDesc(menuDesc: String) {
        _menudesc.value = menuDesc
    }

    fun addMenu(
        image: MultipartBody.Part,
        idPenjual: String,
        nama: String,
        deskripsi: String,
        harga: String,
        stok: String
    ) {
        viewModelScope.launch {
            try {
                val result = repository.addMenu(idPenjual, nama, deskripsi, harga, stok, image)
                _addMenuResult.value = result
            } catch (e: Exception) {
                _addMenuResult.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }
}