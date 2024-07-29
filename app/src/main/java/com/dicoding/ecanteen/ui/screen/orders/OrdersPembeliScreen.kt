package com.dicoding.ecanteen.ui.screen.orders

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.remote.response.PesananPembeliResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ConfirmDialog
import com.dicoding.ecanteen.ui.components.PesananPembeliModel
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.screen.transaction.OrdersPembeliViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import com.dicoding.ecanteen.ui.theme.not_ready_container
import com.dicoding.ecanteen.ui.theme.ready_container

@Composable
fun OrdersPembeliScreen(
    modifier: Modifier = Modifier,
    ordersPembeliViewModel: OrdersPembeliViewModel,
    profileViewModel: ProfileViewModel,
    onDetailPesananPembeli: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }
    val userModel by profileViewModel.userModel.collectAsState()

    LaunchedEffect(userModel) {
        profileViewModel.getUserSessionPembeli()
    }

    userModel?.let {
        ordersPembeliViewModel.getPesananPembeli(it.id)
    }

    val pesananPembeliResponseState by ordersPembeliViewModel.pesananPembeliResponse.collectAsState()
    var showFilterButtons by remember { mutableStateOf(false) }
    var filterStatus by remember { mutableStateOf<Int?>(null) }

    val showDialog = remember { mutableStateOf(false) }
    val pesananIdToDelete = remember { mutableStateOf(0) }

    fun showDeleteConfirmationDialog(id: Int) {
        pesananIdToDelete.value = id
        showDialog.value = true
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.orders_pembeli_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            when (pesananPembeliResponseState) {
                ResultState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ResultState.Success -> {
                    val pesananPembeliResponse =
                        (pesananPembeliResponseState as ResultState.Success<PesananPembeliResponse>).data

                    if (pesananPembeliResponse.dataPesananPembeli.isNullOrEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_data),
                                contentDescription = stringResource(R.string.no_data_img),
                                modifier = Modifier.size(200.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.no_data),
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        val dataItems =
                            pesananPembeliResponse.dataPesananPembeli.filter { dataItem ->
                                filterStatus == null || dataItem?.status?.toInt() == filterStatus
                            }

                        LazyColumn(
                            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                        ) {
                            val reversedDataItems = dataItems.reversed()
                            val (statusZero, statusOne) = reversedDataItems.partition { it?.status?.toInt() == 0 }
                            val sortedDataItems = statusZero + statusOne

                            items(sortedDataItems) { dataItem ->
                                if (dataItem != null) {
                                    PesananPembeliModel(
                                        modifier = Modifier,
                                        pesanan = dataItem,
                                        onDetailPesananPembeli = {
                                            dataItem.id?.let { it1 ->
                                                onDetailPesananPembeli(
                                                    it1.toInt()
                                                )
                                            }
                                        },
                                        onDeleteClick = {
                                            dataItem.id?.let { showDeleteConfirmationDialog(it.toInt()) }
                                        },
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                }
                            }
                        }
                    }
                }

                is ResultState.Error -> {
                    Log.e(
                        "OrdersPembeliScreen",
                        "Error: ${(pesananPembeliResponseState as ResultState.Error).errorMessage}"
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        // button list by ready or not ready
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            if (showFilterButtons) {
                AnimatedVisibility(
                    visible = showFilterButtons,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .border(
                                    1.dp,
                                    color = MaterialTheme.colorScheme.error,
                                    shape = CircleShape
                                )
                                .background(not_ready_container, shape = CircleShape)
                                .clickable {
                                    filterStatus = 0
                                    showFilterButtons = false
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.padding(12.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        Box(
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    color = Color.hsl(
                                        hue = 120f,
                                        saturation = 1.0f,
                                        lightness = 0.20f
                                    ),
                                    shape = CircleShape
                                )
                                .background(ready_container, shape = CircleShape)
                                .clickable {
                                    filterStatus = 1
                                    showFilterButtons = false
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.padding(12.dp),
                                tint = Color.hsl(
                                    hue = 120f,
                                    saturation = 1.0f,
                                    lightness = 0.20f
                                ),
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .clickable {
                        showFilterButtons = !showFilterButtons
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(42.dp),
                    tint = Color.White
                )
            }
        }

        if (showDialog.value) {
            ConfirmDialog(
                onConfirm = {
                    userModel?.let {
                        ordersPembeliViewModel.deletePesananFromId(it.id, pesananIdToDelete.value)
                    }
                },
                onDismiss = {
                    showDialog.value = false
                }
            )
        }
    }
}
