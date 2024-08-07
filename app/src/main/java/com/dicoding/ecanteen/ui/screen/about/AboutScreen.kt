package com.dicoding.ecanteen.ui.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.ui.theme.fontFamily

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp, top = 20.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .clickable { onBackClick() }
            )
        }
        LazyColumn(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 46.dp, start = 16.dp, end = 16.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo_ecanteen),
                    contentDescription = stringResource(R.string.sign_in_image),
                    modifier = Modifier
                        .size(250.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.about),
                    fontFamily = fontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}