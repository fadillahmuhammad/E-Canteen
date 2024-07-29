package com.dicoding.ecanteen.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.local.pref.MenuModel
import com.dicoding.ecanteen.data.local.pref.UserModel
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _menus = MutableStateFlow<ResultState<List<MenuModel>>>(ResultState.Loading)
    val menus: StateFlow<ResultState<List<MenuModel>>> get() = _menus

    private val _userModel = MutableStateFlow<UserModel?>(null)
    val userModel: StateFlow<UserModel?> get() = _userModel

    private val _querySearch = mutableStateOf("")
    val querySearch: State<String> get() = _querySearch

    init {
        viewModelScope.launch {
            val user = repository.getUserSessionPenjual()
            _userModel.value = user
            user?.let {
                getMenusFromId(it.id)
            }
        }
    }

    fun getMenus() {
        viewModelScope.launch {
            try {
                _menus.value = ResultState.Loading
                val getMenus = repository.getMenus()
                _menus.value = ResultState.Success(getMenus)
            } catch (e: Exception) {
                _menus.value = ResultState.Error("Failed to fetch menus: ${e.message}")
            }
        }
    }

    fun getMenusFromId(idPenjual: Int) {
        viewModelScope.launch {
            try {
                _menus.value = ResultState.Loading
                val result = repository.getMenusFromId(idPenjual)
                _menus.value = result
            } catch (e: Exception) {
                _menus.value = ResultState.Error("Failed to fetch menus: ${e.message}")
            }
        }
    }

    fun deleteMenuFromId(id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.deleteMenuFromId(id)
                when (result) {
                    is ResultState.Success -> {
                        userModel.value?.let { getMenusFromId(it.id) }
                    }

                    is ResultState.Error -> {

                    }

                    else -> {}
                }
            } catch (e: Exception) {

            }
        }
    }

    fun search(newQuery: String) {
        _querySearch.value = newQuery
        performSearch()
    }

    fun clearQuery() {
        _querySearch.value = ""
        getMenus()
    }

    private fun performSearch() {
        viewModelScope.launch {
            val query = _querySearch.value
            if (query.isNotEmpty()) {
                try {
                    _menus.value = ResultState.Loading
                    val filteredMenus = repository.getMenus().filter { it.nama.contains(query, ignoreCase = true) }
                    _menus.value = ResultState.Success(filteredMenus)
                } catch (e: Exception) {
                    _menus.value = ResultState.Error("Failed to fetch menus: ${e.message}")
                }
            } else {
                getMenus()
            }
        }
    }
}
