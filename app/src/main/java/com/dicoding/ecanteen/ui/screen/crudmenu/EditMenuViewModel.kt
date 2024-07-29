package com.dicoding.ecanteen.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.remote.response.AddMenuResponse
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditMenuViewModel(private val repository: UserRepository) : ViewModel() {

    private val _menuState = MutableStateFlow<ResultState<AddMenuResponse>>(ResultState.Loading)
    val menuState: StateFlow<ResultState<AddMenuResponse>> = _menuState

    private val _editMenuState = MutableStateFlow<ResultState<AddMenuResponse>>(ResultState.Loading)
    val editMenuState: StateFlow<ResultState<AddMenuResponse>> = _editMenuState

    private val _menuname = mutableStateOf("")
    val menuname: State<String> = _menuname

    private val _menuprice = mutableStateOf("")
    val menuprice: State<String> = _menuprice

    private val _menustok = mutableStateOf("")
    val menustok: State<String> = _menustok

    private val _menudesc = mutableStateOf("")
    val menudesc: State<String> = _menudesc

    private val _menuImageUrl = mutableStateOf<String?>(null)
    val menuImageUrl: State<String?> = _menuImageUrl

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

    fun setMenuImageUrl(url: String?) {
        _menuImageUrl.value = url
    }

    fun fetchMenuById(id: Int) {
        viewModelScope.launch {
            _menuState.value = ResultState.Loading
            val result = repository.getMenuById(id)
            if (result is ResultState.Success) {
                val menu = result.data.menu
                menu?.let {
                    setMenuName(it.nama ?: "")
                    setMenuPrice(it.harga ?: "")
                    setMenuStok(it.stok ?: "")
                    setMenuDesc(it.deskripsi ?: "")
                    setMenuImageUrl(it.gambar ?: "")
                }
            }
            _menuState.value = result
        }
    }

    fun editMenu(
        id: Int,
        idPenjual: Int,
        name: String,
        description: String,
        price: String,
        stock: String,
        imagePart: MultipartBody.Part?,
    ) {
        viewModelScope.launch {
            _editMenuState.value = ResultState.Loading
            val result = repository.editMenu(id, idPenjual, name, description, price, stock, imagePart)
            _editMenuState.value = result
        }
    }
}
