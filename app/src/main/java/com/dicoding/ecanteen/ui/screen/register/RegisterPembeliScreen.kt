package com.dicoding.ecanteen.ui.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.di.Injection
import com.dicoding.ecanteen.data.local.pref.UserModel
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.components.TextFieldEmailModel
import com.dicoding.ecanteen.ui.components.TextFieldModel
import com.dicoding.ecanteen.ui.components.TextFieldNumberModel
import com.dicoding.ecanteen.ui.components.TextFieldPasswordModel
import com.dicoding.ecanteen.ui.theme.EcanteenTheme
import com.dicoding.ecanteen.ui.theme.fontFamily
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun RegisterPembeliScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onButtonClick: () -> Unit,
    viewModel: RegisterViewModel
) {
    var isShowingSuccess by remember { mutableStateOf(false) }
    var isShowingError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val registerResult: ResultState<UserModel> by viewModel.registerResult.collectAsState()
    val listState = rememberLazyListState()
    val isFullNameEmpty = viewModel.fullname.value.isEmpty()
    val isEmailEmpty = viewModel.email.value.isEmpty()
    val isPasswordEmpty = viewModel.password.value.isEmpty()
    val isEmailError =
        viewModel.email.value.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(viewModel.email.value)
            .matches()
    val isPasswordError =
        viewModel.password.value.isNotEmpty() && viewModel.password.value.length < 8

    LaunchedEffect(isShowingSuccess, isShowingError, isLoading) {
        if (isShowingSuccess || isShowingError || isLoading) {
            delay(2000)
            isLoading = false
            isShowingSuccess = false
            isShowingError = false
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 24.dp, horizontal = 24.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .clickable { onBackClick() }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.sign_up_buyer),
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.sing_up_img),
                        contentDescription = stringResource(R.string.sign_up_image),
                        modifier = Modifier
                            .size(250.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.enter_your_full_name),
                        value = viewModel.fullname.value,
                        onValueChange = { viewModel.setFullName(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextFieldNumberModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.enter_your_telp),
                        value = viewModel.telp.value,
                        onValueChange = { viewModel.setTelp(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextFieldEmailModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.enter_your_email),
                        value = viewModel.email.value,
                        onValueChange = { viewModel.setEmail(it) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldPasswordModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.enter_your_password),
                        value = viewModel.password.value,
                        onValueChange = { viewModel.setPassword(it) }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    ButtonModel(
                        text = stringResource(R.string.register),
                        contentDesc = stringResource(R.string.btn_register),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        onClick = {
                            isLoading = true
                            val uid = UUID.randomUUID().toString().replace("-", "").substring(0, 12).uppercase()
                            viewModel.registerPembeli(
                                viewModel.fullname.value,
                                viewModel.telp.value,
                                uid,
                                viewModel.email.value,
                                viewModel.password.value,
                            )
                        },
                        enabled = !isFullNameEmpty && !isEmailEmpty && !isPasswordEmpty && !isEmailError && !isPasswordError
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        LaunchedEffect(registerResult) {
                            when (registerResult) {
                                is ResultState.Success -> {
                                    isShowingSuccess = true
                                    delay(1500)
                                    isShowingSuccess = false
                                    onButtonClick()
                                }

                                is ResultState.Error -> {
                                    isShowingError = true
                                    delay(4000)
                                    isShowingError = false
                                }

                                else -> {}
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.register_text),
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                        ClickableText(
                            text = AnnotatedString(stringResource(R.string.text_click_login)),
                            modifier = Modifier.clickable {},
                            style = TextStyle(
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            onClick = { onButtonClick() },
                        )
                    }
                }
            }
        }
        if (isShowingSuccess) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                content = {
                    Text(text = stringResource(R.string.register_success_notif))
                }
            )
        } else if (isShowingError) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.error,
                content = {
                    when (val failedResult = registerResult) {
                        is ResultState.Error -> {
                            val message = failedResult.errorMessage
                            Text(text = message)
                        }

                        else -> {}
                    }
                }
            )
        }
    }

}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
fun RegisterPembeliScreenPreview() {
    val userRepository = Injection.provideRepository()
    EcanteenTheme {
        RegisterPembeliScreen(
            onBackClick = {},
            onButtonClick = {},
            viewModel = RegisterViewModel(
                userRepository
            )
        )
    }
}