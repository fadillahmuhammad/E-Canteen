package com.dicoding.ecanteen.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.di.Injection
import com.dicoding.ecanteen.data.local.pref.ProfilePenjualModel
import com.dicoding.ecanteen.data.remote.response.GetProfilePenjualResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.theme.EcanteenTheme
import com.dicoding.ecanteen.ui.theme.fontFamily

@Composable
fun ProfilePenjualScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    onAboutClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onEditProfile: (ProfilePenjualModel) -> Unit,
    viewModel: ProfileViewModel,
) {
    val getProfilePenjualState: ResultState<GetProfilePenjualResponse> by viewModel.getProfilePenjualState.collectAsState()
    val listState = rememberLazyListState()
    val userModel by viewModel.userModel.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userModel) {
        viewModel.getUserSessionPenjual()
    }

    userModel?.let {
        viewModel.getProfilePenjual(it.id)
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 19.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.profile),
                fontFamily = fontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(19.dp))
            when (getProfilePenjualState) {
                is ResultState.Loading -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                clip = true,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Color.Black.copy(alpha = 0.35f)
                            )
                            .clickable {

                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        )
                    ){
                        Column(
                            modifier
                                .fillMaxWidth()
                                .padding(vertical = 37.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                is ResultState.Success -> {
                    val getProfilePenjualResponse =
                        (getProfilePenjualState as ResultState.Success).data

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                clip = true,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Color.Black.copy(alpha = 0.35f)
                            )
                            .clickable {
                                val profileData = ProfilePenjualModel(
                                    id = getProfilePenjualResponse.profile?.id?.toInt() ?: 0,
                                    fullName = getProfilePenjualResponse.profile?.namaLengkap ?: "",
                                    email = getProfilePenjualResponse.profile?.username ?: "",
                                    telp = getProfilePenjualResponse.profile?.telp ?: "",
                                    namaToko = getProfilePenjualResponse.profile?.namaToko ?: "",
                                    deskripsi = getProfilePenjualResponse.profile?.deskripsi ?: "",
                                )
                                onEditProfile(profileData)
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(75.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(top = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val initial =
                                    getProfilePenjualResponse.profile?.namaLengkap?.firstOrNull()
                                        ?.toString()?.uppercase() ?: ""

                                Text(
                                    text = initial,
                                    fontFamily = fontFamily,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier
                            ) {
                                Text(
                                    text = getProfilePenjualResponse.profile?.namaLengkap ?: "",
                                    fontFamily = fontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = getProfilePenjualResponse.profile?.username ?: "",
                                    fontFamily = fontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = getProfilePenjualResponse.profile?.telp ?: "",
                                    fontFamily = fontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }

                is ResultState.Error -> {

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        clip = true,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.35f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onHistoryClick()
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.history_ic),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(R.string.history),
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(3f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.go_to_arrow),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAboutClick()
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.about_ic),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(R.string.about_ecanteen),
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(3f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.go_to_arrow),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    showLogoutDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .height(49.dp)
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = stringResource(R.string.logout_icon),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.logout),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(text = stringResource(R.string.logout))
            },
            text = {
                Text(text = stringResource(R.string.logout_confirmation))
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                        viewModel.clearUserModel()
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.no))
                }
            }
        )
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
fun ProfilePenjualScreenPreview() {
    EcanteenTheme {
        val userRepository = Injection.provideRepository()
        ProfilePenjualScreen(
            onLogoutClick = {},
            onAboutClick = {},
            viewModel = ProfileViewModel(userRepository),
            onHistoryClick = {},
            onEditProfile = {}
        )
    }
}