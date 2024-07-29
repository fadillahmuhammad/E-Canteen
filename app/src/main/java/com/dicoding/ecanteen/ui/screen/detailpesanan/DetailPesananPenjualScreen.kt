package com.dicoding.ecanteen.ui.screen.detailpesanan

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.theme.fontFamily
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private fun formatRupiah(amount: Int?): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp. ${format.format(amount)}"
}

private fun convertToJakartaTime(timestamp: String?): String {
    if (timestamp == null) return "-"

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    val utcDate = sdf.parse(timestamp) ?: return "-"

    sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

    val outputFormat = SimpleDateFormat("d-MM-yyyy | HH:mm", Locale.getDefault())
    return outputFormat.format(utcDate)
}

@Composable
fun DetailPesananPenjualScreen(
    modifier: Modifier = Modifier,
    pesananId: Int,
    onBackClick: () -> Unit,
    detailPesananPenjualViewModel: DetailPesananPenjualViewModel
) {
    detailPesananPenjualViewModel.getDetailPesananPenjual(pesananId)
    val detailPesananPenjualResponseState by detailPesananPenjualViewModel.detailPesananPenjualResponse.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
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
                Spacer(modifier = Modifier.width(56.dp))
                Text(
                    text = stringResource(R.string.detail_order_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            when (detailPesananPenjualResponseState) {
                ResultState.Loading -> {
                    Column(
                        Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                is ResultState.Success -> {
                    val detailPesananPenjualResponse =
                        (detailPesananPenjualResponseState as ResultState.Success).data

                    val name = detailPesananPenjualResponse.dataDetailPesananPenjual?.nama
                    val noPesanan = detailPesananPenjualResponse.dataDetailPesananPenjual?.noPesanan
                    val qty = detailPesananPenjualResponse.dataDetailPesananPenjual?.qty
                    val catatan = detailPesananPenjualResponse.dataDetailPesananPenjual?.catatan
                    val jamPesan = detailPesananPenjualResponse.dataDetailPesananPenjual?.jamPesan
                    val jmlHarga = detailPesananPenjualResponse.dataDetailPesananPenjual?.jmlHarga
                    val tanggal = detailPesananPenjualResponse.dataDetailPesananPenjual?.tanggal
                    val status = detailPesananPenjualResponse.dataDetailPesananPenjual?.status
                    val harga = detailPesananPenjualResponse.dataDetailPesananPenjual?.harga
                    val namaLengkap =
                        detailPesananPenjualResponse.dataDetailPesananPenjual?.namaLengkap
                    val telp = detailPesananPenjualResponse.dataDetailPesananPenjual?.telp
                    val imageUrl = detailPesananPenjualResponse.dataDetailPesananPenjual?.gambar
                    val imageUrlValue =
                        "https://ecanteenunpam.000webhostapp.com/ecanteen/images/${imageUrl}"

                    val jamPesanConvert = jamPesan?.substring(0, 5) ?: "-"

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
                                            text = name.toString(),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                        )
                                        Text(
                                            text = formatRupiah(harga?.toInt()),
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                        )
                                        if (status?.toInt() == 0) {
                                            Text(
                                                text = stringResource(R.string.not_ready),
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.error,
                                                fontWeight = FontWeight.SemiBold,
                                            )
                                        } else {
                                            Text(
                                                text = stringResource(R.string.ready),
                                                fontSize = 16.sp,
                                                color = Color.hsl(
                                                    hue = 120f,
                                                    saturation = 1.0f,
                                                    lightness = 0.20f
                                                ),
                                                fontWeight = FontWeight.SemiBold,
                                            )
                                        }
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
                                        text = noPesanan.toString(),
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
                                        text = convertToJakartaTime(tanggal),
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
                                        text = jamPesanConvert,
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
                                        text = qty.toString(),
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
                                        text = catatan.toString(),
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
                                        text = formatRupiah(harga?.toInt()) + " x " + qty,
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
                                        text = formatRupiah(jmlHarga?.toInt()),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                    )
                                }
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 14.dp)
                                )
                                Text(
                                    text = stringResource(R.string.info_pembeli),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.detail_nama_lengkap),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = namaLengkap.toString(),
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
                                        text = stringResource(R.string.detail_telp),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        text = telp.toString(),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                    )
                                }
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 14.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.detail_status),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    if (status?.toInt() == 0) {
                                        Text(
                                            text = stringResource(R.string.not_ready),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    } else {
                                        Text(
                                            text = stringResource(R.string.ready),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is ResultState.Error -> {
                    Log.e(
                        "DetailPesananScreen",
                        "Error: ${(detailPesananPenjualResponseState as ResultState.Error).errorMessage}"
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