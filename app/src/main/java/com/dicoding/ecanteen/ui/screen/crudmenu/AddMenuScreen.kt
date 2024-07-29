package com.dicoding.ecanteen.ui.screen.crudmenu

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.local.pref.MenuAddModel
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.components.TextFieldLongModel
import com.dicoding.ecanteen.ui.components.TextFieldModel
import com.dicoding.ecanteen.ui.components.TextFieldNumberModel
import com.dicoding.ecanteen.ui.components.TextFieldNumberStokModel
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@Composable
fun AddMenuScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onButtonClick: () -> Unit,
    viewModel: AddMenuViewModel,
    profileViewModel: ProfileViewModel,
) {
    var isShowingSuccess by remember { mutableStateOf(false) }
    var isShowingError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val addMenuResult: ResultState<MenuAddModel> by viewModel.addMenuResult.collectAsState()
    val isNameEmpty = viewModel.menuname.value.isEmpty()
    val isPriceEmpty = viewModel.menuprice.value.isEmpty()
    val isStokEmpty = viewModel.menustok.value.isEmpty()
    var idPenjual: Int? by remember { mutableStateOf(null) }
    val userModel by profileViewModel.userModel.collectAsState()

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    fun compressImage(uri: Uri, context: Context): MultipartBody.Part? {
        return try {
            val fileName =
                "${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val outputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            outputStream.close()

            Log.d(TAG, "File sementara terkompresi dibuat: ${file.path}")

            val requestBody = file.asRequestBody("image/jpeg".toMediaType())
            MultipartBody.Part.createFormData("gambar", file.name, requestBody)
        } catch (e: Exception) {
            Log.e(TAG, "Error converting Uri to Part: ${e.message}")
            null
        }
    }

    LaunchedEffect(userModel) {
        profileViewModel.getUserSessionPenjual()

        userModel?.let {
            idPenjual = it.id
        }
    }

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
                Spacer(modifier = Modifier.width(54.dp))
                Text(
                    text = stringResource(R.string.add_menu_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 46.dp)
            ) {
                item {
                    Box(
                        Modifier
                            .size(130.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            selectedImageUri?.let {
                                Image(
                                    painter = rememberImagePainter(it),
                                    contentDescription = stringResource(R.string.selected_image),
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            } ?: Image(
                                painter = painterResource(id = R.drawable.lunch_dining),
                                contentDescription = stringResource(R.string.default_image),
                                modifier = Modifier.size(70.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 0.dp)
                                .size(34.dp)
                                .clip(CircleShape)
                                .clickable {
                                    imagePickerLauncher.launch("image/*")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = stringResource(R.string.add_image),
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextFieldModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.menu_name),
                        value = viewModel.menuname.value,
                        onValueChange = { viewModel.setMenuName(it) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldNumberModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.menu_price),
                        value = viewModel.menuprice.value,
                        onValueChange = { viewModel.setMenuPrice(it) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldNumberStokModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.menu_stok),
                        value = viewModel.menustok.value,
                        onValueChange = { viewModel.setMenuStok(it) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldLongModel(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.menu_desc),
                        value = viewModel.menudesc.value,
                        onValueChange = { viewModel.setMenuDesc(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonModel(
                        text = stringResource(R.string.btn_add_menu),
                        contentDesc = stringResource(R.string.btn_addMenu),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        onClick = {
                            if (selectedImageUri != null && idPenjual != null) {
                                isLoading = true
                                val nama = viewModel.menuname.value
                                val deskripsi = viewModel.menudesc.value
                                val harga = viewModel.menuprice.value
                                val stok = viewModel.menustok.value

                                selectedImageUri?.let { uri ->
                                    val imagePart = compressImage(uri, context)
                                    imagePart?.let {
                                        viewModel.addMenu(
                                            it,
                                            idPenjual.toString(),
                                            nama,
                                            deskripsi,
                                            harga,
                                            stok
                                        )
                                    }
                                }
                            }
                        },
                        enabled = selectedImageUri != null && !isNameEmpty && !isPriceEmpty && !isStokEmpty
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
                        LaunchedEffect(addMenuResult) {
                            when (addMenuResult) {
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
        if (isShowingSuccess) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                content = {
                    Text(text = stringResource(R.string.add_success_notif))
                }
            )
        } else if (isShowingError) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.error,
                content = {
                    when (val failedResult = addMenuResult) {
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