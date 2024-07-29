package com.dicoding.ecanteen.ui.screen.home

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.di.Injection
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.CardStore
import com.dicoding.ecanteen.ui.components.SearchModel
import com.dicoding.ecanteen.ui.theme.EcanteenTheme

@Composable
fun HomePembeliScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onMenuClick: (Int) -> Unit,
    onPesanClick: (Int) -> Unit,
) {
    val context = LocalContext.current
    BackHandler {
        context.findActivity()?.finish()
    }

    val menuState by viewModel.menus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMenus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Banner(
            viewModel = viewModel,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (menuState) {
                is ResultState.Loading -> {
                    CircularProgressIndicator()
                }

                is ResultState.Success -> {
                    val menus = (menuState as ResultState.Success).data

                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, bottom = 16.dp, end = 16.dp)
                    ) {
                        items(menus) { menu ->
                            CardStore(
                                modifier = modifier.padding(bottom = 22.dp),
                                menu = menu,
                                onMenuClick = onMenuClick,
                                onPesanClick = onPesanClick
                            )
                        }
                    }
                }

                is ResultState.Error -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    Box(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(120.dp)
        )
        SearchModel(
            query = viewModel.querySearch.value,
            onSearch = viewModel::search,
            onClear = viewModel::clearQuery,
            padding = 16
        )
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
fun HomeScreenPreview() {
    EcanteenTheme {
        val userRepository = Injection.provideRepository()
        HomePembeliScreen(
            viewModel = HomeViewModel(userRepository),
            onMenuClick = {},
            onPesanClick = {}
        )
    }
}