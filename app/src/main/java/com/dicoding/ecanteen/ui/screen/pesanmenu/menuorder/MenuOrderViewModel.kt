package com.dicoding.ecanteen.ui.screen.pesanmenu.menuorder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dicoding.ecanteen.data.UserRepository

class MenuOrderViewModel(private val repository: UserRepository) : ViewModel() {

    private val _menudesc = mutableStateOf("")
    val menudesc: State<String> = _menudesc

    private val _menuclock = mutableStateOf("")
    val menuclock: State<String> = _menuclock

    fun setMenuDesc(menuDesc: String) {
        _menudesc.value = menuDesc
    }

    fun setMenuClock(menuClock: String) {
        _menuclock.value = menuClock
    }
}
