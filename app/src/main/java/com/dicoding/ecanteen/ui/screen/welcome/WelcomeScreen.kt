package com.dicoding.ecanteen.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.theme.fontFamily

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navigateToRegisterPenjual: () -> Unit,
    navigateToRegisterPembeli: () -> Unit,
    navigateToLoginPenjual: () -> Unit,
    navigateToLoginPembeli: () -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 40.dp, horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_ecanteen),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = stringResource(R.string.welcome_title),
            fontFamily = fontFamily,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            fontSize = 22.sp
        )
        Text(
            text = stringResource(R.string.welcome_sub),
            fontFamily = fontFamily,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(26.dp))
        ButtonModel(
            text = stringResource(R.string.register_penjual),
            contentDesc = stringResource(R.string.button_register_penjual),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            onClick = { navigateToRegisterPenjual() }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ButtonModel(
            text = stringResource(R.string.register_pembeli),
            contentDesc = stringResource(R.string.button_register_pembeli),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            onClick = { navigateToRegisterPembeli() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Atau",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonModel(
            text = stringResource(R.string.login_penjual),
            contentDesc = stringResource(R.string.button_login_penjual),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            onClick = { navigateToLoginPenjual() }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ButtonModel(
            text = stringResource(R.string.login_pembeli),
            contentDesc = stringResource(R.string.button_login_pembeli),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            onClick = { navigateToLoginPembeli() }
        )
    }
}