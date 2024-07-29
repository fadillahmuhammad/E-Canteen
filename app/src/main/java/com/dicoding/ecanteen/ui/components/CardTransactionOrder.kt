package com.dicoding.ecanteen.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dicoding.ecanteen.data.remote.response.DataItem
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

    val outputFormat = SimpleDateFormat("EEEE, d MMMM yyyy - HH:mm", Locale.getDefault())
    return outputFormat.format(utcDate)
}

@Composable
fun CardTransactionOrder(
    modifier: Modifier = Modifier,
    transaksi: DataItem
) {
    val imageUrlValue = transaksi.gambar?.let {
        "https://ecanteenunpam.000webhostapp.com/ecanteen/images/$it"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageUrlValue != null) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(90.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
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
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier,
        ) {
            Text(
                text = transaksi.nama.toString() ?: "-",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = formatRupiah(transaksi.jumlahTransaksi),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = convertToJakartaTime(transaksi.tanggalTransaksi),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}
