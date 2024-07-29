package com.dicoding.ecanteen.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.ui.theme.EcanteenTheme
import com.dicoding.ecanteen.ui.theme.fontFamily

@Composable
fun TextFieldNumberStokModel(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val validatedValue = remember(value) {
        if (value.all { it.isDigit() }) value else ""
    }

    OutlinedTextField(
        value = validatedValue,
        onValueChange = { newText ->
            if (newText.all { it.isDigit() }) {
                onValueChange(newText)
            }
        },
        label = {
            Text(
                text = label,
                fontSize = 16.sp
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            cursorColor = Color.Black,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Black,
            disabledBorderColor = Color.Black,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        trailingIcon = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.increase),
                    modifier = Modifier
                        .clickable {
                            val newValue = (validatedValue.toIntOrNull() ?: 0) + 1
                            onValueChange(newValue.toString())
                        }
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.decrease),
                    modifier = Modifier
                        .clickable {
                            val newValue = (validatedValue.toIntOrNull() ?: 0) - 1
                            if (newValue >= 0) {
                                onValueChange(newValue.toString())
                            }
                        }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldNumberStokModelPreview() {
    EcanteenTheme {
        val value = remember { mutableStateOf("") }
        TextFieldNumberStokModel(
            label = "Stok",
            value = value.value,
            onValueChange = { value.value = it }
        )
    }
}
