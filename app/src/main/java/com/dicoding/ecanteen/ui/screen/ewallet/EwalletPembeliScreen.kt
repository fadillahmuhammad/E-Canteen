package com.dicoding.ecanteen.ui.screen.ewallet

import EwalletPembeliViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.remote.response.TransaksiSaldoResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.CardTransactionOrder
import com.dicoding.ecanteen.ui.components.CardTransactionTopup
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import java.text.NumberFormat
import java.util.Locale

private fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "IDR ${format.format(amount)}"
}

@Composable
fun EwalletPembeliScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    ewalletViewModel: EwalletPembeliViewModel,
    onTopUpScreen: (Int) -> Unit
) {
    val userModel by profileViewModel.userModel.collectAsState()

    LaunchedEffect(userModel) {
        profileViewModel.getUserSessionPembeli()
    }

    userModel?.let {
        ewalletViewModel.getSaldo(it.id, "pembeli")
        ewalletViewModel.getTransaksiSaldo(it.id, "pembeli")
    }

    val saldoResponseState by ewalletViewModel.saldoResponse.collectAsState()
    val transaksiSaldoResponseState by ewalletViewModel.transaksiSaldoResponse.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.ewallet_title),
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(20.dp)),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(20.dp)),
                        painter = painterResource(id = R.drawable.card_ewallet),
                        contentDescription = null,
                    )
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))
                        when (saldoResponseState) {
                            ResultState.Loading -> {
                                Column(
                                    modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                            }

                            is ResultState.Success -> {
                                val saldoResponse = (saldoResponseState as ResultState.Success).data
                                Text(
                                    text = formatRupiah(saldoResponse.data?.jumlahSaldo ?: 0),
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Text(
                                            text = "Nama Pemilik",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraLight,
                                            color = Color.White
                                        )
                                        Text(
                                            text = saldoResponse.data?.namaPemilik ?: "-",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light,
                                            color = Color.White
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Text(
                                            text = "ID Kartu",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraLight,
                                            color = Color.White
                                        )
                                        Text(
                                            text = saldoResponse.data?.idKartu ?: "-",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            is ResultState.Error -> {
                                Log.e(
                                    "EwalletScreen",
                                    "Error: ${(saldoResponseState as ResultState.Error).errorMessage}"
                                )
                                Column(
                                    modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                            }
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.height(18.dp)
                )
                Text(
                    text = "History Transaksi"
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                when (transaksiSaldoResponseState) {
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
                        val transaksiSaldoResponse =
                            (transaksiSaldoResponseState as ResultState.Success<TransaksiSaldoResponse>).data

                        if (transaksiSaldoResponse?.data.isNullOrEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_transaction),
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                            ) {
                                transaksiSaldoResponse?.data?.let { dataItems ->
                                    val reversedDataItems = dataItems.reversed()
                                    items(reversedDataItems) { dataItem ->
                                        if (dataItem != null) {
                                            if (dataItem.jenisTransaksi == "topup") {
                                                CardTransactionTopup(
                                                    modifier = Modifier,
                                                    transaksi = dataItem
                                                )
                                                Spacer(modifier = Modifier.height(14.dp))
                                            } else if (dataItem.jenisTransaksi == "beli_menu") {
                                                CardTransactionOrder(
                                                    modifier = Modifier,
                                                    transaksi = dataItem
                                                )
                                                Spacer(modifier = Modifier.height(14.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is ResultState.Error -> {
                        Log.e(
                            "EwalletScreen",
                            "Error: ${(transaksiSaldoResponseState as ResultState.Error).errorMessage}"
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
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        ) {
            val saldoId = (saldoResponseState as? ResultState.Success)?.data?.data?.id
            Image(
                painter = painterResource(id = R.drawable.top_up_btn),
                contentDescription = null,
                modifier = Modifier
                    .clickable { saldoId?.let { onTopUpScreen(it) } }
                    .padding(12.dp)
                    .size(42.dp)
            )
        }
    }
}
