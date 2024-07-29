package com.dicoding.ecanteen.ui.screen.topup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.components.TextFieldNumberModel
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return format.format(amount)
}

@Composable
fun TopUpScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSuccessScreen: () -> Unit,
    saldoId: Int,
    topUpViewModel: TopUpViewModel,
    profileViewModel: ProfileViewModel
) {
    val userModel by profileViewModel.userModel.collectAsState()
    val amount by topUpViewModel.amount.collectAsState()

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(userModel) {
        profileViewModel.getUserSessionPembeli()
    }

    val amounts = listOf(
        "5000",
        "10000",
        "15000",
        "20000",
        "25000",
        "50000",
        "100000",
        "250000",
        "500000"
    )

    val isAmountValid = remember(amount) {
        amount.isNotEmpty() && (amount.toIntOrNull() ?: 0) >= 5000
    }

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
                Spacer(modifier = Modifier.width(71.dp))
                Text(
                    text = stringResource(R.string.top_up_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                TextFieldNumberModel(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.amount),
                    value = amount,
                    onValueChange = { value ->
                        topUpViewModel.setAmount(value)
                        val selected = amounts.contains(value)
                        topUpViewModel.setSelectedAmount(if (selected) value else "")
                    },
                )
                Text(
                    text = "* Minimal isi saldo sebesar IDR 5.000",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Pilih Jumlah",
                )
                Spacer(modifier = Modifier.height(16.dp))

                for (i in amounts.indices step 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (j in 0..2) {
                            if (i + j < amounts.size) {
                                val amountOption = amounts[i + j]
                                val isSelected = topUpViewModel.selectedAmount.value == amountOption

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp)
                                        .clickable {
                                            topUpViewModel.setSelectedAmount(amountOption)
                                            topUpViewModel.setAmount(amountOption)
                                        }
                                        .then(
                                            if (isSelected) {
                                                Modifier.background(
                                                    MaterialTheme.colorScheme.secondaryContainer,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                            } else {
                                                Modifier.border(
                                                    1.dp,
                                                    Color.Gray,
                                                    RoundedCornerShape(10.dp)
                                                )
                                            }
                                        )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp, horizontal = 4.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "IDR",
                                            fontWeight = FontWeight.Light,
                                            color = MaterialTheme.colorScheme.outline,
                                        )
                                        Text(text = formatRupiah(amountOption.toInt()), fontSize = 14.sp)
                                    }
                                }
                                if (j < 2) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (isLoading) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    ButtonModel(
                        text = stringResource(R.string.btn_top_up),
                        contentDesc = stringResource(R.string.btn_topup),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        enabled = isAmountValid,
                        onClick = {
                            userModel?.let {
                                isLoading = true
                                topUpViewModel.topUpSaldo(
                                    idUser = it.id,
                                    tipeUser = "pembeli",
                                    idSaldo = saldoId,
                                    amount = amount.toIntOrNull() ?: 0
                                )

                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(1500)
                                    isLoading = false
                                    onSuccessScreen()
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
