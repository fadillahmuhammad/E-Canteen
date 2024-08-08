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
import coil.compose.rememberAsyncImagePainter
import com.dicoding.ecanteen.R
import com.dicoding.ecanteen.data.remote.response.AddMenuResponse
import com.dicoding.ecanteen.ui.common.ResultState
import com.dicoding.ecanteen.ui.components.ButtonModel
import com.dicoding.ecanteen.ui.components.TextFieldLongModel
import com.dicoding.ecanteen.ui.components.TextFieldModel
import com.dicoding.ecanteen.ui.components.TextFieldNumberModel
import com.dicoding.ecanteen.ui.components.TextFieldNumberStokModel
import com.dicoding.ecanteen.ui.screen.home.EditMenuViewModel
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.theme.fontFamily
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@Composable
fun EditMenuScreen(
    menuId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onButtonClick: () -> Unit,
    profileViewModel: ProfileViewModel,
    editMenuViewModel: EditMenuViewModel,
) {
    var isShowingSuccess by remember { mutableStateOf(false) }
    var isShowingError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val isNameEmpty = editMenuViewModel.menuname.value.isEmpty()
    val isPriceEmpty = editMenuViewModel.menuprice.value.isEmpty()
    val isStokEmpty = editMenuViewModel.menustok.value.isEmpty()
    val editMenuState: ResultState<AddMenuResponse> by editMenuViewModel.editMenuState.collectAsState()
    var idPenjual: Int? by remember { mutableStateOf(null) }
    val menuState by editMenuViewModel.menuState.collectAsState()
    val userModel by profileViewModel.userModel.collectAsState()

    LaunchedEffect(userModel) {
        profileViewModel.getUserSessionPenjual()

        userModel?.let {
            idPenjual = it.id
        }
    }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    fun compressImage(uri: Uri, context: Context): MultipartBody.Part? {
        return try {
            val fileName = "${System.currentTimeMillis()}.jpg"
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

    fun uriToFile(context: Context): MultipartBody.Part? {
        return try {
            val gambar = editMenuViewModel.menuImageUrl.value

            val fileName = gambar
            val file = File(context.cacheDir, fileName)

            Log.d(TAG, "File sementara terkompresi dibuat: ${file.path}")

            val requestBody = file.asRequestBody("image/jpeg".toMediaType())
            MultipartBody.Part.createFormData("gambar", file.name, requestBody)
        } catch (e: Exception) {
            Log.e(TAG, "Error converting Uri to Part: ${e.message}")
            null
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

    LaunchedEffect(menuId) {
        editMenuViewModel.fetchMenuById(menuId)
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
                    text = stringResource(R.string.edit_menu_title),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (menuState) {
                    is ResultState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is ResultState.Success -> {
                        val menu = (menuState as ResultState.Success<AddMenuResponse>).data.menu

                        Log.d(TAG, "Menu data: $menu")

                        menu?.let {
                            LaunchedEffect(menu) {
                                editMenuViewModel.setMenuName(it.nama ?: "")
                                editMenuViewModel.setMenuPrice(it.harga.toString())
                                editMenuViewModel.setMenuStok(it.stok.toString())
                                editMenuViewModel.setMenuDesc(it.deskripsi ?: "")
                                editMenuViewModel.setMenuImageUrl(it.gambar ?: "")
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
                                                    painter = rememberAsyncImagePainter(it),
                                                    contentDescription = stringResource(R.string.selected_image),
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop,
                                                )
                                            } ?: run {
                                                val imageUrl = editMenuViewModel.menuImageUrl.value
                                                val imageUrlValue =
                                                    "https://my-absen.my.id/ecanteen/images/${imageUrl}"
                                                if (imageUrl != null) {
                                                    Image(
                                                        painter = rememberAsyncImagePainter(model = imageUrlValue),
                                                        contentDescription = stringResource(R.string.add_image),
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = ContentScale.Crop,
                                                    )
                                                } else {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.lunch_dining),
                                                        contentDescription = stringResource(R.string.default_image),
                                                        modifier = Modifier.size(70.dp),
                                                        colorFilter = ColorFilter.tint(Color.White)
                                                    )
                                                }
                                            }
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
                                        value = editMenuViewModel.menuname.value,
                                        onValueChange = { editMenuViewModel.setMenuName(it) }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextFieldNumberModel(
                                        modifier = Modifier.fillMaxWidth(),
                                        label = stringResource(R.string.menu_price),
                                        value = editMenuViewModel.menuprice.value,
                                        onValueChange = { editMenuViewModel.setMenuPrice(it) },
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextFieldNumberStokModel(
                                        modifier = Modifier.fillMaxWidth(),
                                        label = stringResource(R.string.menu_stok),
                                        value = editMenuViewModel.menustok.value,
                                        onValueChange = { editMenuViewModel.setMenuStok(it) }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextFieldLongModel(
                                        modifier = Modifier.fillMaxWidth(),
                                        label = stringResource(R.string.menu_desc),
                                        value = editMenuViewModel.menudesc.value,
                                        onValueChange = { editMenuViewModel.setMenuDesc(it) }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    ButtonModel(
                                        text = stringResource(R.string.btn_edit_menu),
                                        contentDesc = stringResource(R.string.btn_editMenu),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White
                                        ),
                                        onClick = {
                                            isLoading = true
                                            val nama = editMenuViewModel.menuname.value
                                            val deskripsi = editMenuViewModel.menudesc.value
                                            val harga = editMenuViewModel.menuprice.value
                                            val stok = editMenuViewModel.menustok.value

                                            if (selectedImageUri != null && idPenjual != null) {
                                                selectedImageUri?.let { uri ->
                                                    val imagePart = compressImage(uri, context)
                                                    imagePart?.let {
                                                        editMenuViewModel.editMenu(
                                                            menuId,
                                                            idPenjual!!,
                                                            nama,
                                                            deskripsi,
                                                            harga,
                                                            stok,
                                                             it
                                                        )
                                                    }
                                                }
                                            } else if (idPenjual != null) {
                                                // Jika tidak ada gambar baru yang dipilih, gunakan URL gambar yang ada
                                                val imagePart = uriToFile(context)
                                                imagePart?.let {
                                                    editMenuViewModel.editMenu(
                                                        menuId,
                                                        idPenjual!!,
                                                        nama,
                                                        deskripsi,
                                                        harga,
                                                        stok,
                                                        it
                                                    )
                                                }
                                            }
                                        },
                                        enabled = !isNameEmpty && !isPriceEmpty && !isStokEmpty
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(24.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        LaunchedEffect(editMenuState) {
                                            when (editMenuState) {
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

                    is ResultState.Error -> {
                        val errorMessage = (menuState as ResultState.Error).errorMessage
                        Log.e(TAG, "Error fetching menu: $errorMessage")

                        Column(
                            modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = Color.White)
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
                    Text(text = stringResource(R.string.edit_success_notif))
                }
            )
        } else if (isShowingError) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.error,
                content = {
                    when (val failedResult = editMenuState) {
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
