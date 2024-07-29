package com.dicoding.ecanteen.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.local.pref.MenuModel
import com.dicoding.ecanteen.ui.theme.EcanteenTheme
import com.dicoding.ecanteen.ui.theme.fontFamily
import java.text.NumberFormat
import java.util.Locale

private fun formatRupiah(amount: Int?): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp. ${format.format(amount)}"
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CardMenu(
    menu: MenuModel,
    modifier: Modifier = Modifier,
    onEditClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    onMenuClick: (Int) -> Unit,
) {
    val imageUrl = "https://ecanteenunpam.000webhostapp.com/ecanteen/images/${menu.gambar}"

    Card(
        modifier = modifier
            .width(358.dp)
            .height(200.dp)
            .shadow(
                elevation = 8.dp,
                clip = true,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.35f)
            )
            .clickable {
                onMenuClick(menu.id)
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(160.dp)
            )
            Column(
                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = menu.nama,
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Stok: " + menu.stok.toString(),
                    fontFamily = fontFamily,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                ) {
                    Image(
                        painter = painterResource(R.drawable.money_ic),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formatRupiah(menu.harga),
                        fontFamily = fontFamily,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = menu.deskripsi,
                    fontFamily = fontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = modifier) {
                    TextContainerModel(
                        text = stringResource(R.string.edit),
                        ph = 8.dp,
                        pv = 3.dp,
                        shape = 12.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        onClick = { onEditClick(menu.id) }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TextContainerModel(
                        text = stringResource(R.string.delete),
                        ph = 8.dp,
                        pv = 3.dp,
                        shape = 12.dp,
                        color = MaterialTheme.colorScheme.error,
                        onClick = { onDeleteClick() }
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
fun CardMenuPreview() {
    EcanteenTheme {
        val sampleMenu = MenuModel(
            id = 1,
            id_penjual = 1,
            nama = "Nasi Goreng",
            gambar = "nasi_goreng.jpg",
            deskripsi = "Nasi goreng enak dengan ayam dan telur",
            harga = 15000,
            stok = 10,
            nama_toko = "Warung Makan",
            nama_lengkap = "Amin",
            penjual_deskripsi = "apa aja",
            message = "tes",
            penjual_telp = "aaa"
        )
        CardMenu(
            menu = sampleMenu,
            modifier = Modifier.padding(16.dp),
            onEditClick = {},
            onDeleteClick = {},
            onMenuClick = {}
        )
    }
}
