package com.dicoding.ecanteen.ui.components

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dicoding.ecanteen.data.local.pref.MenuModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import java.text.NumberFormat
import java.util.Locale

private fun formatRupiah(amount: Int?): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp. ${format.format(amount)}"
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CardStore(
    menu: MenuModel,
    modifier: Modifier = Modifier,
    onMenuClick: (Int) -> Unit,
    onPesanClick: (Int) -> Unit,
) {
    val imageUrl = "https://my-absen.my.id/ecanteen/images/${menu.gambar}"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
            .shadow(
                elevation = 8.dp,
                clip = true,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.35f)
            )
            .clickable {
                onMenuClick(menu.id)
                Log.d(TAG, "ID DARI MENU: ${menu.id}")
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(208.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.0f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 1f)
                                    ),
                                    startY = 200f
                                )
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = menu.nama,
                                fontFamily = fontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.scrim,
                            )
                            TextContainerModel(
                                text = "Pesan",
                                ph = 14.dp,
                                pv = 4.dp,
                                shape = 12.dp,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp,
                                onClick = {
                                          onPesanClick(menu.id)
                                },
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "Stok " + menu.stok,
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "•",
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatRupiah(menu.harga) + " seporsi",
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        Text(
                            text = menu.deskripsi,
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                TextContainerModel(
                    text = menu.nama_toko ?: "",
                    ph = 14.dp,
                    pv = 5.dp,
                    shape = 12.dp,
                    color = Color.hsl(
                        hue = 120f,
                        saturation = 1.0f,
                        lightness = 0.20f
                    ),
                    modifier = Modifier
                        .align(
                            Alignment.TopStart
                        ),
                    onClick = {},
                )
            }
        }
    }
}