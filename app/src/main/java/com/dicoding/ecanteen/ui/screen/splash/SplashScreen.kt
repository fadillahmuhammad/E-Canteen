package com.dicoding.ecanteen.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.ui.theme.EcanteenTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onTimeout: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_ecanteen),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
    }

    LaunchedEffect(true) {
        delay(3000)
        onTimeout()
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
fun SplashScreenView() {
    EcanteenTheme {
        SplashScreen(onTimeout = { })
    }
}
