package com.dicoding.ecanteen.ui.screen.pesanmenu.menuorder

import android.app.TimePickerDialog
import android.content.ContentValues
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.local.pref.TransactionModel
import com.dicoding.ecanteen.data.remote.response.AddMenuResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.components.TextFieldLongModel
import com.dicoding.ecanteen.ui.screen.home.EditMenuViewModel
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

private fun formatRupiah(amount: Int?): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp. ${format.format(amount)}"
}

@Composable
fun MenuOrderScreen(
    modifier: Modifier = Modifier,
    menuId: Int,
    onBackClick: () -> Unit,
    onTransaction: (TransactionModel) -> Unit,
    profileViewModel: ProfileViewModel,
    editMenuViewModel: EditMenuViewModel,
    menuOrderViewModel: MenuOrderViewModel
) {
    val userModel by profileViewModel.userModel.collectAsState()

    LaunchedEffect(userModel) {
        profileViewModel.getUserSessionPembeli()
    }

    val menuState by editMenuViewModel.menuState.collectAsState()

    LaunchedEffect(menuId) {
        editMenuViewModel.fetchMenuById(menuId)
    }

    val name = editMenuViewModel.menuname.value
    val priceText = editMenuViewModel.menuprice.value
    val price = priceText.toIntOrNull() ?: 0
    val stok = editMenuViewModel.menustok.value.toIntOrNull() ?: 0
    val imageUrl = editMenuViewModel.menuImageUrl.value
    val imageUrlValue =
        "https://my-absen.my.id/ecanteen/images/${imageUrl}"

    val stokColor = if (stok < 10) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var time by remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            val selectedTime = String.format("%02d:%02d", hour, minute)
            menuOrderViewModel.setMenuClock(selectedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    var quantity by remember { mutableStateOf(1) }
    val totalPrice = price * quantity

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .fillMaxWidth(),
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
                    text = stringResource(R.string.menu_order_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            when (menuState) {
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
                    val menu = (menuState as ResultState.Success<AddMenuResponse>).data.menu

                    Log.d(ContentValues.TAG, "Menu data: $menu")

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
                                                    text = name,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                )
                                                Text(
                                                    text = formatRupiah(
                                                        priceText.toIntOrNull() ?: 0
                                                    ),
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp,
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Divider(
                                            modifier = Modifier.padding(vertical = 14.dp)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.menu_stok),
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Text(
                                                text = stok.toString(),
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp,
                                                color = stokColor
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Jumlah Pesan",
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Row(
                                                modifier = Modifier,
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            MaterialTheme.colorScheme.primary,
                                                            shape = RoundedCornerShape(8.dp),
                                                        )
                                                        .clickable {
                                                            if (quantity > 1) {
                                                                quantity -= 1
                                                            }
                                                        }
                                                        .padding(vertical = 2.dp, horizontal = 9.dp)
                                                ) {
                                                    Text(
                                                        text = "-",
                                                        color = Color.White
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .width(23.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = quantity.toString(),
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = 16.sp,
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            MaterialTheme.colorScheme.primary,
                                                            shape = RoundedCornerShape(8.dp),
                                                        )
                                                        .clickable {
                                                            if (quantity < stok) {
                                                                quantity += 1
                                                            }
                                                        }
                                                        .padding(vertical = 2.dp, horizontal = 8.dp)
                                                ) {
                                                    Text(
                                                        text = "+",
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Jumlah Harga",
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = formatRupiah(price) + " x $quantity:",
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Text(
                                                text = formatRupiah(totalPrice),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.order_time),
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = if (menuOrderViewModel.menuclock.value.isEmpty()) "Pilih Jam" else menuOrderViewModel.menuclock.value,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp,
                                                    color = if (menuOrderViewModel.menuclock.value.isEmpty()) Color.Gray else Color.Black,
                                                    modifier = Modifier
                                                        .clickable {
                                                            timePickerDialog.show()
                                                        }
                                                        .padding(4.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        TextFieldLongModel(
                                            modifier = Modifier.fillMaxWidth(),
                                            label = stringResource(R.string.order_note),
                                            value = menuOrderViewModel.menudesc.value,
                                            onValueChange = { menuOrderViewModel.setMenuDesc(it) }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
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
                            ButtonModel(
                                text = stringResource(R.string.btn_next_menu),
                                contentDesc = stringResource(R.string.btn_buyMenu),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    userModel?.let {
                                        val uid =
                                            UUID.randomUUID().toString().replace("-", "")
                                                .substring(0, 12)
                                                .uppercase()
                                        val transactionData = menu?.idPenjual?.let { it1 ->
                                            imageUrl?.let { it2 ->
                                                TransactionModel(
                                                    orderId = uid,
                                                    menuId = menuId,
                                                    nama = name,
                                                    gambar = it2,
                                                    pembeliId = it.id,
                                                    penjualId = it1.toInt(),
                                                    quantity = quantity,
                                                    catatan = menuOrderViewModel.menudesc.value,
                                                    jam_pesan = menuOrderViewModel.menuclock.value,
                                                    harga = price,
                                                    jml_harga = totalPrice,
                                                    status = false
                                                )
                                            }
                                        }
                                        if (transactionData != null) {
                                            onTransaction(transactionData)
                                        }
                                    }
                                },
                                enabled = !menuOrderViewModel.menuclock.value.isEmpty()
                            )
                        }
                    }
                }

                is ResultState.Error -> {
                    val errorMessage = (menuState as ResultState.Error).errorMessage
                    Log.e(ContentValues.TAG, "Error fetching menu: $errorMessage")

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
