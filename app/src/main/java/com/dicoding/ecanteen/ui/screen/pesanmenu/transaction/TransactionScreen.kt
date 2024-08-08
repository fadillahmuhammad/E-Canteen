package com.dicoding.ecanteen.ui.screen.detailpesanan

import EwalletPembeliViewModel
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.remote.response.PesanMenuResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.screen.pesanmenu.transaction.TransactionViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private fun formatRupiah(amount: Int?): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp. ${format.format(amount)}"
}

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSuccessOrderScreen: () -> Unit,
    ewalletViewModel: EwalletPembeliViewModel,
    transactionViewModel: TransactionViewModel,
    orderId: String,
    menuId: Int,
    nama: String,
    gambar: String,
    pembeliId: Int,
    penjualId: Int,
    quantity: Int,
    catatan: String,
    jam_pesan: String,
    harga: Int,
    jml_harga: Int,
    status: Boolean,
) {
    val pesanMenuState: ResultState<PesanMenuResponse> by transactionViewModel.pesanMenuState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    ewalletViewModel.getSaldo(pembeliId, "pembeli")
    val saldoResponseState by ewalletViewModel.saldoResponse.collectAsState()

    val imageUrlValue =
        "https://my-absen.my.id/ecanteen/images/${gambar}"

    val currentDate = remember {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        dateFormat.format(calendar.time)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier

        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.width(56.dp))
                Text(
                    text = stringResource(R.string.confirmation_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            when (saldoResponseState) {
                is ResultState.Loading -> {
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
                    val saldoResponse = (saldoResponseState as ResultState.Success).data

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxSize(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 46.dp)
                            ) {
                                item {
                                    Column(
                                        modifier = modifier
                                    ) {
                                        Row(
                                            modifier = Modifier
                                        ) {
                                            AsyncImage(
                                                model = imageUrlValue,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .height(100.dp)
                                                    .border(
                                                        width = 3.dp,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        shape = RoundedCornerShape(20.dp),
                                                    )
                                                    .clip(
                                                        shape = RoundedCornerShape(20.dp)
                                                    )
                                                    .background(color = MaterialTheme.colorScheme.primary)
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column(
                                                modifier = Modifier
                                            ) {
                                                Text(
                                                    text = nama,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                )
                                                Text(
                                                    text = formatRupiah(harga),
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp,
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 14.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.detail_pesanan),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.detail_no_pesanan),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Text(
                                                text = orderId,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.detail_tanggal),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.outline,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = currentDate,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                textAlign = TextAlign.Right,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.detail_jam_pesan),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Text(
                                                text = jam_pesan,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.detail_quantity),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Text(
                                                text = quantity.toString(),
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.detail_catatan),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.outline,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = if (catatan.isNotEmpty()) catatan else "-",
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                textAlign = TextAlign.Right,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 14.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.rincian_biaya),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.detail_harga),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Text(
                                                text = formatRupiah(harga) + " x " + quantity,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.detail_jumlah_harga),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Text(
                                                text = formatRupiah(jml_harga),
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                            )
                                        }
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 14.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(
                                modifier = Modifier
                            ) {
                                // TELAH DIBANTU OLEH NISFU RAMADHANI
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total Pembayaran: ",
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        text = formatRupiah(jml_harga),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Dompet Kamu: ",
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        text = formatRupiah(saldoResponse.data?.jumlahSaldo ?: 0),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = if (saldoResponse.data?.jumlahSaldo!! > jml_harga) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                if (saldoResponse.data?.jumlahSaldo!! < jml_harga) {
                                    Text(
                                        text = "* Maaf, saldo kamu tidak cukup!",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 14.sp,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                if (isLoading) {
                                    LaunchedEffect(isLoading) {
                                        delay(1000)
                                        onSuccessOrderScreen()
                                    }
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                } else {
                                    ButtonModel(
                                        text = stringResource(R.string.btn_order_menu),
                                        contentDesc = stringResource(R.string.btn_buyMenu),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White
                                        ),
                                        onClick = {
                                            transactionViewModel.pesanMenu(
                                                idPembeli = pembeliId,
                                                idPenjual = penjualId,
                                                idMenu = menuId,
                                                noPesanan = orderId,
                                                qty = quantity,
                                                catatan = catatan,
                                                jamPesan = jam_pesan,
                                                jmlHarga = jml_harga,
                                                status = status
                                            )
                                            isLoading = true
                                        },
                                        enabled = saldoResponse.data?.jumlahSaldo!! > jml_harga
                                    )
                                }
                            }
                        }
                    }
                }

                is ResultState.Error -> {
                    Log.e(
                        "TransactionScreen",
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
}