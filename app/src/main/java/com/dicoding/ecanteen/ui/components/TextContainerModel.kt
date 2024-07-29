package com.dicoding.ecanteen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.ui.theme.fontFamily

@Composable
fun TextContainerModel(
    modifier: Modifier = Modifier,
    text: String,
    ph: Dp,
    pv: Dp,
    shape: Dp,
    color: Color,
    fontSize: TextUnit = 12.sp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(shape)
            )
            .clickable { onClick() }
            .padding(
                horizontal = ph,
                vertical = pv
            )
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}