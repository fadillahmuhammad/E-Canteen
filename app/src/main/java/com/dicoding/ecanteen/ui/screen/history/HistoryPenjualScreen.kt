package com.dicoding.ecanteen.ui.screen.history

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.remote.response.HistoryPenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.CardHistoryPenjual
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.theme.container_primary
import com.dicoding.ecanteen.ui.theme.fontFamily
import com.dicoding.ecanteen.ui.theme.ready_container
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

private fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp ${format.format(amount)}"
}

@Composable
fun HistoryPenjualScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    historyPenjualViewModel: HistoryPenjualViewModel,
    onBackClick: () -> Unit,
) {
    val userModel by profileViewModel.userModel.collectAsState()

    val selectedYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val selectedMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }

    LaunchedEffect(userModel) {
        profileViewModel.getUserSessionPenjual()
    }

    userModel?.let {
        LaunchedEffect(selectedYear.value, selectedMonth.value) {
            historyPenjualViewModel.getHistoryPenjual(
                it.id,
                selectedYear.value,
                selectedMonth.value + 1
            )
        }
    }

    val historyPenjualResponseState by historyPenjualViewModel.historyPenjualResponse.collectAsState()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.width(64.dp))
                Text(
                    text = stringResource(R.string.history_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.heightIn(16.dp))
            MonthYearPicker(
                selectedMonth = selectedMonth,
                selectedYear = selectedYear
            )
            Spacer(modifier = Modifier.heightIn(26.dp))
            when (historyPenjualResponseState) {
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
                    val historyPenjualResponse =
                        (historyPenjualResponseState as ResultState.Success<HistoryPenjualResponse>).data

                    if (historyPenjualResponse?.dataHistoryPenjual.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(
                                        color = container_primary,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(20.dp)
                                    ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null,
                                        modifier = Modifier.size(42.dp)
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = "Total \nPesanan",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = "0",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(
                                        color = ready_container,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color.hsl(
                                            hue = 120f,
                                            saturation = 1.0f,
                                            lightness = 0.20f
                                        ),
                                        RoundedCornerShape(20.dp)
                                    ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        tint =  Color.hsl(
                                            hue = 120f,
                                            saturation = 1.0f,
                                            lightness = 0.20f
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(42.dp)
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = "Total \nPemasukan",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = formatRupiah(
                                            0
                                        ),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color =  Color.hsl(
                                            hue = 120f,
                                            saturation = 1.0f,
                                            lightness = 0.20f
                                        ),
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.heightIn(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_history),
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(
                                        color = container_primary,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(20.dp)
                                    ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null,
                                        modifier = Modifier.size(42.dp)
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = "Total \nPesanan",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = historyPenjualResponse.jumlahPesanan.toString(),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(
                                        color = ready_container,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color.hsl(
                                            hue = 120f,
                                            saturation = 1.0f,
                                            lightness = 0.20f
                                        ),
                                        RoundedCornerShape(20.dp)
                                    ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        tint = Color.hsl(
                                            hue = 120f,
                                            saturation = 1.0f,
                                            lightness = 0.20f
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(42.dp)
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = "Total \nPemasukan",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Spacer(modifier = Modifier.heightIn(8.dp))
                                    Text(
                                        text = formatRupiah(
                                            historyPenjualResponse.totalHarga?.toIntOrNull() ?: 0
                                        ),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.hsl(
                                            hue = 120f,
                                            saturation = 1.0f,
                                            lightness = 0.20f
                                        )
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.heightIn(24.dp))
                        Text(
                            text = stringResource(R.string.sub_menu_history)
                        )
                        Spacer(modifier = Modifier.heightIn(4.dp))
                        LazyColumn(
                            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                        ) {
                            historyPenjualResponse.dataHistoryPenjual?.let {
                                items(it.reversed()) { data ->
                                    if (data != null) {
                                        CardHistoryPenjual(history = data)
                                        Spacer(modifier = Modifier.height(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                is ResultState.Error -> {
                    Log.e(
                        "HistoryPenjualScreen",
                        "Error: ${(historyPenjualResponseState as ResultState.Error).errorMessage}"
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
    }
}

@Composable
private fun MonthYearPicker(
    selectedMonth: MutableState<Int>,
    selectedYear: MutableState<Int>,
) {
    val months = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )
    val years = (2020..2030).toList()

    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Tampilkan Menurut",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
            ) {
                Button(
                    onClick = { expandedMonth = true },
                    modifier = Modifier
                        .height(34.dp),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 14.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Unspecified,
                        contentColor = MaterialTheme.colorScheme.outline,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = months[selectedMonth.value],
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
                DropdownMenu(
                    expanded = expandedMonth,
                    onDismissRequest = { expandedMonth = false },
                ) {
                    months.forEachIndexed { index, month ->
                        DropdownMenuItem(
                            onClick = {
                                selectedMonth.value = index
                                expandedMonth = false
                            },
                            text = {
                                Text(text = month)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
            ) {
                Button(
                    onClick = { expandedYear = true },
                    modifier = Modifier
                        .height(34.dp),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 14.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Unspecified,
                        contentColor = MaterialTheme.colorScheme.outline,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedYear.value.toString(),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
                DropdownMenu(
                    expanded = expandedYear,
                    onDismissRequest = { expandedYear = false }
                ) {
                    years.forEach { year ->
                        DropdownMenuItem(
                            onClick = {
                                selectedYear.value = year
                                expandedYear = false
                            },
                            text = {
                                Text(text = year.toString())
                            }
                        )
                    }
                }
            }
        }
    }
}