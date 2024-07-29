package com.dicoding.ecanteen.ui.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.remote.response.DataPesananPenjualItem
import com.dicoding.ecanteen.ui.theme.not_ready_container
import com.dicoding.ecanteen.ui.theme.ready_container
import java.text.NumberFormat
import java.util.Locale

private fun formatRupiah(amount: Int?): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp. ${format.format(amount)}"
}

@Composable
fun PesananPenjualModel(
    modifier: Modifier = Modifier,
    pesanan: DataPesananPenjualItem,
    onDetailPesananPenjual: (Int) -> Unit,
    onUpdateStatus: (Int, Int) -> Unit
) {
    val containerColor = if (pesanan.status?.toInt() == 0) not_ready_container else ready_container
    val borderColor = if (pesanan.status?.toInt() == 0) MaterialTheme.colorScheme.error else Color.hsl(hue = 120f, saturation = 1.0f, lightness = 0.20f)
    val jamPesan = pesanan.jamPesan?.substring(0, 5) ?: "-"

    val imageUrlValue = pesanan.gambar?.let {
        "https://ecanteenunpam.000webhostapp.com/ecanteen/images/$it"
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { pesanan.id?.let { onDetailPesananPenjual(it.toInt()) } }
                .border(
                    2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(color = containerColor, shape = RoundedCornerShape(20.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageUrlValue != null) {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(140.dp)
                        .clip(shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                ) {
                    AsyncImage(
                        model = imageUrlValue,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(90.dp))
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = pesanan.nama ?: "-",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = pesanan.qty ?: "-",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Harga",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = formatRupiah(pesanan.jmlHarga?.toInt()),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Untuk Jam",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = jamPesan ?: "-",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline
                    )
                    if (pesanan.status?.toInt() == 0) {
                        Text(
                            text = stringResource(R.string.not_ready),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold,
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.ready),
                            fontSize = 14.sp,
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
        }
        if(pesanan.status?.toInt() == 0){
            Box(
                modifier = Modifier
                    .clickable {
                        pesanan.id?.let { onUpdateStatus(it.toInt(), 1) }
                    }
                    .background(color = borderColor, shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 10.dp)),
                contentAlignment = Alignment.TopStart
            ) {
                // jika sedang di proses atau loading, maka diganti dengan CircularProgressIndicator()
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clickable {
                        pesanan.id?.let { onUpdateStatus(it.toInt(), 0) }
                    }
                    .background(color = borderColor, shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 10.dp)),
                contentAlignment = Alignment.TopStart
            ) {
                // jika sedang di proses atau loading, maka diganti dengan CircularProgressIndicator()
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}
