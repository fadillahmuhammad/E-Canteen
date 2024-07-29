package com.dicoding.ecanteen.ui.screen.detail

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
import com.dicoding.ecanteen.data.remote.response.AddMenuResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.screen.home.EditMenuViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import java.text.NumberFormat
import java.util.Locale

private fun formatRupiah(amount: Int?): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp. ${format.format(amount)}"
}

@Composable
fun DetailMenuPembeli(
    modifier: Modifier = Modifier,
    menuId: Int,
    onBackClick: () -> Unit,
    onMenuOrder: (Int) -> Unit,
    editMenuViewModel: EditMenuViewModel,
) {
    val menuState by editMenuViewModel.menuState.collectAsState()

    LaunchedEffect(menuId) {
        editMenuViewModel.fetchMenuById(menuId)
    }

    val name = editMenuViewModel.menuname.value
    val priceString = editMenuViewModel.menuprice.value
    val price = priceString.takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0

    val stokString = editMenuViewModel.menustok.value
    val stok = stokString.takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0

    val desc = editMenuViewModel.menudesc.value
    val imageUrl = editMenuViewModel.menuImageUrl.value
    val imageUrlValue = "https://ecanteenunpam.000webhostapp.com/ecanteen/images/${imageUrl}"

    val stokColor = if (stok < 10) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
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
                    text = stringResource(R.string.detail_menu_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (menuState) {
                    is ResultState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is ResultState.Success -> {
                        val menu = (menuState as ResultState.Success<AddMenuResponse>).data.menu

                        Log.d(ContentValues.TAG, "Menu data: $menu")

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 46.dp)
                        ) {
                            item {
                                Column(
                                    modifier = modifier
                                ) {
                                    AsyncImage(
                                        model = imageUrlValue,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
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
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = name,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 24.sp,
                                    )
                                    Divider(
                                        modifier = Modifier.padding(vertical = 14.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.detail_menu),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = stringResource(R.string.menu_price),
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        Text(
                                            text = formatRupiah(price.toInt()),
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
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
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.menu_desc),
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = desc,
                                        textAlign = TextAlign.Right,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                ButtonModel(
                    text = stringResource(R.string.btn_buy_menu),
                    contentDesc = stringResource(R.string.btn_buyMenu),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    onClick = {
                        onMenuOrder(menuId)
                    },
                )
            }
        }
    }
}
