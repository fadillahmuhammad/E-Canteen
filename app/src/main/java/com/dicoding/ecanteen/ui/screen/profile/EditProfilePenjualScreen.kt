package com.dicoding.ecanteen.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.remote.response.EditProfilePenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.components.TextFieldLongModel
import com.dicoding.ecanteen.ui.components.TextFieldModel
import com.dicoding.ecanteen.ui.components.TextFieldNumberModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import kotlinx.coroutines.delay

@Composable
fun EditProfilePenjualScreen(
    modifier: Modifier = Modifier,
    id: Int,
    fullName: String,
    email: String,
    telp: String,
    namaToko: String,
    deskripsi: String,
    onBackClick: () -> Unit,
    onButtonClick: () -> Unit,
    editProfilePenjualViewModel: EditProfilePenjualViewModel
) {
    val editProfilePenjualState: ResultState<EditProfilePenjualResponse> by editProfilePenjualViewModel.editProfilePenjualState.collectAsState()
    var isShowingSuccess by remember { mutableStateOf(false) }
    var isShowingError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var fullNameState by remember { mutableStateOf(fullName) }
    var telpState by remember { mutableStateOf(telp) }
    var namaTokoState by remember { mutableStateOf(namaToko) }
    var deskripsiState by remember { mutableStateOf(deskripsi) }

    val isFullNameEmpty = fullNameState.isEmpty()
    val isTelpEmpty = telpState.isEmpty()
    val isNamaTokoEmpty = namaTokoState.isEmpty()

    val initial = fullName.firstOrNull()?.toString()?.uppercase() ?: ""

    val context = LocalContext.current

    LaunchedEffect(isShowingSuccess, isShowingError, isLoading) {
        if (isShowingSuccess || isShowingError || isLoading) {
            delay(2000)
            isLoading = false
            isShowingSuccess = false
            isShowingError = false
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
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
                    text = stringResource(R.string.edit_profile_pembeli_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 46.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(top = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initial,
                                fontFamily = fontFamily,
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        Spacer(modifier = Modifier.height(26.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { },
                            label = {
                                Text(
                                    text = stringResource(R.string.profile_email),
                                    fontSize = 16.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.outline
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
                            readOnly = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextFieldModel(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.profile_fullname),
                            value = fullNameState,
                            onValueChange = { fullNameState = it }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextFieldNumberModel(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.profile_telp),
                            value = telpState,
                            onValueChange = { telpState = it },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextFieldModel(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.profile_namatoko),
                            value = namaTokoState,
                            onValueChange = { namaTokoState = it }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextFieldLongModel(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.profile_deskripsi),
                            value = deskripsiState,
                            onValueChange = { deskripsiState = it }
                        )
                        Spacer(modifier = Modifier.height(56.dp))
                        ButtonModel(
                            text = stringResource(R.string.btn_edit_menu),
                            contentDesc = stringResource(R.string.btn_editMenu),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            onClick = {
                                isLoading = true
                                editProfilePenjualViewModel.editProfilePenjual(
                                    id,
                                    fullNameState,
                                    telpState,
                                    namaTokoState,
                                    deskripsiState
                                )
                            },
                            enabled = !isFullNameEmpty && !isTelpEmpty && !isNamaTokoEmpty
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            LaunchedEffect(editProfilePenjualState) {
                                when (editProfilePenjualState) {
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
                    Text(text = stringResource(R.string.edit_profile_success_notif))
                }
            )
        } else if (isShowingError) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.error,
                content = {
                    when (val failedResult = editProfilePenjualState) {
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
