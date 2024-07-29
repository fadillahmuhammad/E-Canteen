package com.dicoding.ecanteen.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.data.local.pref.MenuModel
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.CardMenu
import com.dicoding.ecanteen.ui.components.ConfirmDialog
import com.dicoding.ecanteen.ui.components.TextContainerModel
import com.dicoding.ecanteen.ui.theme.fontFamily

@Composable
fun HomePenjualScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onMenuClick: (Int) -> Unit,
) {
    val menuState by viewModel.menus.collectAsState()
    val userModel by viewModel.userModel.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val menuIdToDelete = remember { mutableStateOf(0) }

    fun showDeleteConfirmationDialog(id: Int) {
        menuIdToDelete.value = id
        showDialog.value = true
    }

    LaunchedEffect(userModel) {
        userModel?.let { viewModel.getMenusFromId(it.id) }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Menu Anda",
                fontFamily = fontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            TextContainerModel(
                text = "+ Tambah Menu",
                ph = 16.dp,
                pv = 8.dp,
                shape = 12.dp,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(4.dp),
                onClick = { onAddClick() }
            )
        }
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (menuState) {
                is ResultState.Loading -> {
                    CircularProgressIndicator()
                }

                is ResultState.Success -> {
                    val menus = (menuState as ResultState.Success<List<MenuModel>>).data

                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(menus) { menu ->
                            CardMenu(
                                modifier = modifier.padding(bottom = 16.dp),
                                menu = menu,
                                onEditClick = onEditClick,
                                onDeleteClick = {
                                    showDeleteConfirmationDialog(menu.id)
                                },
                                onMenuClick = onMenuClick
                            )
                        }
                    }
                }

                is ResultState.Error -> {
                    CircularProgressIndicator()
                }
            }
        }

        if (showDialog.value) {
            ConfirmDialog(
                onConfirm = {
                    viewModel.deleteMenuFromId(menuIdToDelete.value)
                },
                onDismiss = {
                    showDialog.value = false
                }
            )
        }
    }
}