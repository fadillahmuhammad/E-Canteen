package com.dicoding.ecanteen.data

import android.util.Log
import com.dicoding.ecanteen.data.local.pref.MenuAddModel
import com.dicoding.ecanteen.data.local.pref.MenuModel
import com.dicoding.ecanteen.data.local.pref.UserModel
import com.dicoding.ecanteen.data.local.pref.UserPreference
import com.dicoding.ecanteen.data.remote.response.AddMenuResponse
import com.dicoding.ecanteen.data.remote.response.DetailPesananPembeliResponse
import com.dicoding.ecanteen.data.remote.response.DetailPesananPenjualResponse
import com.dicoding.ecanteen.data.remote.response.EditProfilePembeliResponse
import com.dicoding.ecanteen.data.remote.response.EditProfilePenjualResponse
import com.dicoding.ecanteen.data.remote.response.GetProfilePembeliResponse
import com.dicoding.ecanteen.data.remote.response.GetProfilePenjualResponse
import com.dicoding.ecanteen.data.remote.response.HistoryPembeliResponse
import com.dicoding.ecanteen.data.remote.response.HistoryPenjualResponse
import com.dicoding.ecanteen.data.remote.response.LoginPembeliResponse
import com.dicoding.ecanteen.data.remote.response.LoginPenjualResponse
import com.dicoding.ecanteen.data.remote.response.PesanMenuResponse
import com.dicoding.ecanteen.data.remote.response.PesananPembeliResponse
import com.dicoding.ecanteen.data.remote.response.PesananPenjualResponse
import com.dicoding.ecanteen.data.remote.response.RegisterResponse
import com.dicoding.ecanteen.data.remote.response.SaldoResponse
import com.dicoding.ecanteen.data.remote.response.TopUpResponse
import com.dicoding.ecanteen.data.remote.response.TransaksiSaldoResponse
import com.dicoding.ecanteen.data.remote.retrofit.ApiService
import com.dicoding.ecanteen.ui.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UserRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val TAG: String = "UserRepository"

    suspend fun saveSessionPenjual(user: UserModel) {
        userPreference.saveSessionPenjual(user)
    }

    suspend fun saveSessionPembeli(user: UserModel) {
        userPreference.saveSessionPembeli(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getUserSessionPenjual(): UserModel? {
        return userPreference.getSessionPenjual().firstOrNull()
    }

    suspend fun getUserSessionPembeli(): UserModel? {
        return userPreference.getSessionPembeli().firstOrNull()
    }

    suspend fun loginPembeli(email: String, password: String): ResultState<UserModel> {
        return try {
            val successResponse = apiService.loginPembeli(email, password)
            Log.d(TAG, "Login API response: $successResponse")
            val userModel = convertToUserModelPembeli(successResponse)
            ResultState.Success(userModel)
        } catch (e: HttpException) {
            Log.e(TAG, "Http error: ${e.code()}")
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "Error Body: $errorBody")

            val errorMessage = when (e.code()) {
                400 -> "Enter correct password!"
                401 -> "User is not registered, Sign Up first"
                else -> "An error occurred"
            }

            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    private fun convertToUserModelPembeli(response: LoginPembeliResponse): UserModel {
        val user = response.payload
            ?: throw IllegalStateException(response.message)

        return UserModel(
            id = (user.id ?: 0) as Int,
            message = response.message ?: "",
            fullName = user.fullName ?: "",
            telp = user.telp ?: "",
            username = user.username ?: "",
            token = response.token ?: "",
            isLogin = true,
            storeName = "",
            desc = "",
            userType = "pembeli"
        )
    }

    suspend fun registerPembeli(
        fullName: String,
        telp: String,
        idKartu: String,
        email: String,
        password: String,
    ): ResultState<UserModel> {
        return try {
            val tipeUser = "pembeli"
            val successResponse =
                apiService.registerPembeli(fullName, telp, idKartu, tipeUser, email, password)
            Log.d(TAG, "Login API response: $successResponse")
            val registerModel = convertToRegisterPembeli(successResponse)
            ResultState.Success(registerModel)
        } catch (e: HttpException) {
            Log.e(TAG, "Http error: ${e.code()}")
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "Error Body: $errorBody")

            val errorMessage = when (e.code()) {
                400 -> "Email exists. No need to register again"
                else -> "An error occurred"
            }

            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    private fun convertToRegisterPembeli(response: RegisterResponse): UserModel {
        val user = response.token
            ?: throw IllegalStateException(response.message)

        return UserModel(
            id = 0,
            message = response.message ?: "",
            token = user ?: "",
            fullName = "",
            telp = "",
            username = "",
            isLogin = false,
            storeName = "",
            desc = "",
            userType = "pembeli"
        )
    }

    suspend fun registerPenjual(
        storeName: String,
        fullName: String,
        telp: String,
        idKartu: String,
        email: String,
        password: String
    ): ResultState<UserModel> {
        return try {
            val tipeUser = "penjual"
            val successResponse =
                apiService.registerPenjual(storeName, fullName, telp, idKartu, tipeUser, email, password)
            Log.d(TAG, "Login API response: $successResponse")
            val registerModel = convertToRegisterPenjual(successResponse)
            ResultState.Success(registerModel)
        } catch (e: HttpException) {
            Log.e(TAG, "Http error: ${e.code()}")
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "Error Body: $errorBody")

            val errorMessage = when (e.code()) {
                400 -> "Email exists. No need to register again"
                else -> "An error occurred"
            }

            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    private fun convertToRegisterPenjual(response: RegisterResponse): UserModel {
        val user = response.token
            ?: throw IllegalStateException(response.message)

        return UserModel(
            id = 0,
            message = response.message ?: "",
            token = user ?: "",
            fullName = "",
            telp = "",
            username = "",
            isLogin = false,
            storeName = "",
            desc = "",
            userType = "penjual"
        )
    }

    suspend fun loginPenjual(email: String, password: String): ResultState<UserModel> {
        return try {
            val successResponse = apiService.loginPenjual(email, password)
            Log.d(TAG, "Login API response: $successResponse")
            val userModel = convertToUserModelPenjual(successResponse)
            ResultState.Success(userModel)
        } catch (e: HttpException) {
            Log.e(TAG, "Http error: ${e.code()}")
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "Error Body: $errorBody")

            val errorMessage = when (e.code()) {
                400 -> "Enter correct password!"
                401 -> "User is not registered, Sign Up first"
                else -> "An error occurred"
            }

            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    private fun convertToUserModelPenjual(response: LoginPenjualResponse): UserModel {
        val user = response.payload
            ?: throw IllegalStateException(response.message)

        return UserModel(
            id = (user.id ?: 0) as Int,
            message = response.message ?: "",
            fullName = user.fullName ?: "",
            telp = user.telp ?: "",
            username = user.username ?: "",
            token = response.token ?: "",
            isLogin = true,
            storeName = user.storeName ?: "",
            desc = user.desc ?: "",
            userType = "penjual"
        )
    }

    suspend fun getMenus() = apiService.getMenus()

    suspend fun getMenusFromId(idPenjual: Int): ResultState<List<MenuModel>> {
        return try {
            val menus = apiService.getMenusFromId(idPenjual)
            ResultState.Success(menus)
        } catch (e: HttpException) {
            Log.e(TAG, "Http error: ${e.code()}")
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "Error Body: $errorBody")

            val errorMessage = when (e.code()) {
                // Define error messages as needed
                else -> "An error occurred"
            }

            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getMenuById(id: Int): ResultState<AddMenuResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMenuById(id)
                ResultState.Success(response)
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    suspend fun deleteMenuFromId(id: Int): ResultState<Unit> {
        val action = "delete"
        return try {
            apiService.deleteMenuFromId(action, id)
            ResultState.Success(Unit)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: "An error occurred"
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun deletePesananFromId(id: Int): ResultState<Unit> {
        val action = "delete"
        return try {
            apiService.deletePesananFromId(action, id)
            ResultState.Success(Unit)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: "An error occurred"
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun addMenu(
        idPenjual: String,
        nama: String,
        deskripsi: String,
        harga: String,
        stok: String,
        image: MultipartBody.Part
    ): ResultState<MenuAddModel> {
        return try {
            val idPenjualBody = idPenjual.toRequestBody("text/plain".toMediaType())
            val namaBody = nama.toRequestBody("text/plain".toMediaType())
            val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaType())
            val hargaBody = harga.toRequestBody("text/plain".toMediaType())
            val stokBody = stok.toRequestBody("text/plain".toMediaType())

            Log.d(
                TAG,
                "Mengirim permintaan ke server dengan data: gambar=$image, idPenjual=$idPenjual, nama=$nama, deskripsi=$deskripsi, harga=$harga, stok=$stok"
            )
            val successResponse = apiService.addMenu(
                idPenjualBody,
                namaBody,
                deskripsiBody,
                hargaBody,
                stokBody,
                image
            )
            Log.d(TAG, "Add Menu API response: $successResponse")
            val menuModel = convertToModelAddMenu(successResponse)
            ResultState.Success(menuModel)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: "An error occurred"
            Log.e(TAG, "HttpException: $errorMessage")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    private fun convertToModelAddMenu(response: AddMenuResponse): MenuAddModel {
        val menu = response.menu
            ?: throw IllegalStateException(response.message)

        return MenuAddModel(
            id = menu.id ?: "",
            message = response.message ?: "",
            id_penjual = menu.idPenjual ?: "",
            nama = menu.nama ?: "",
            deskripsi = menu.deskripsi ?: "",
            harga = menu.harga ?: "",
            stok = menu.stok ?: "",
            gambar = menu.gambar ?: "",
            nama_lengkap = "",
            nama_toko = "",
            penjual_deskripsi = "",
            penjual_telp = ""
        )
    }

    suspend fun editMenu(
        id: Int,
        idPenjual: Int,
        name: String,
        description: String,
        price: String,
        stock: String,
        imagePart: MultipartBody.Part?,
    ): ResultState<AddMenuResponse> {
        return try {
            Log.d(
                TAG,
                "Mulai memanggil API editMenu dengan data: id=$id, name=$name, description=$description, price=$price, stock=$stock"
            )
            val action = "update"
            val response = apiService.editMenu(
                action.toRequestBody("text/plain".toMediaType()),
                id.toString().toRequestBody("text/plain".toMediaType()),
                idPenjual.toString().toRequestBody("text/plain".toMediaType()),
                name.toRequestBody("text/plain".toMediaType()),
                description.toRequestBody("text/plain".toMediaType()),
                price.toRequestBody("text/plain".toMediaType()),
                stock.toRequestBody("text/plain".toMediaType()),
                imagePart,
            )
            Log.d(TAG, "Respons API editMenu: $response")

            if (response.isSuccessful) {
                ResultState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error dalam panggilan API: $errorBody")
                ResultState.Error("Error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getSaldo(idUser: Int, tipeUser: String): ResultState<SaldoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSaldo(idUser, tipeUser)
                if (response.isSuccessful) {
                    ResultState.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getTransaksiSaldo(idUser: Int, tipeUser: String): ResultState<TransaksiSaldoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTransaksiSaldo(idUser, tipeUser)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        ResultState.Success(apiResponse)
                    } else {
                        ResultState.Error("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun topUpSaldo(idUser: Int, tipeUser: String, idSaldo: Int, amount: Int): ResultState<TopUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("UserRepository", "topUpSaldo: idUser=$idUser, tipeUser=$tipeUser, idSaldo=$idSaldo, amount=$amount")

                val response = apiService.topUpSaldo(idUser, tipeUser, idSaldo, amount)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        ResultState.Success(apiResponse)
                    } else {
                        ResultState.Error("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getPesananPembeli(idPembeli: Int): ResultState<PesananPembeliResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPesananPembeli(idPembeli)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        ResultState.Success(apiResponse)
                    } else {
                        ResultState.Error("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getPesananPenjual(idPenjual: Int): ResultState<PesananPenjualResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPesananPenjual(idPenjual)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        ResultState.Success(apiResponse)
                    } else {
                        ResultState.Error("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getDetailPesananPembeli(idPesananPembeli: Int): ResultState<DetailPesananPembeliResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDetailPesananPembeli(idPesananPembeli)
                if (response.isSuccessful) {
                    ResultState.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getDetailPesananPenjual(idPesananPenjual: Int): ResultState<DetailPesananPenjualResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDetailPesananPenjual(idPesananPenjual)
                if (response.isSuccessful) {
                    ResultState.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun editStatusPesanan(id: Int, statusBool: Int): ResultState<Unit> {
        return try {
            apiService.editStatusPesanan(id, statusBool)
            ResultState.Success(Unit)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: "An error occurred"
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun editProfilePembeli(
        id: Int,
        fullName: String,
        telp: String,
    ): ResultState<EditProfilePembeliResponse> {
        return try {
            val action = "update"
            val response = apiService.editProfilePembeli(action, id, fullName, telp)
            Log.d(TAG, "Respons API editMenu: $response")

            if (response.isSuccessful) {
                ResultState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error dalam panggilan API: $errorBody")
                ResultState.Error("Error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getProfilePembeli(
        id: Int,
    ): ResultState<GetProfilePembeliResponse> {
        return try {
            val response = apiService.getProfilePembeli(id)
            Log.d(TAG, "Respons API editMenu: $response")

            if (response.isSuccessful) {
                ResultState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error dalam panggilan API: $errorBody")
                ResultState.Error("Error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun editProfilePenjual(
        id: Int,
        fullName: String,
        telp: String,
        namaToko: String,
        deskripsi: String,
    ): ResultState<EditProfilePenjualResponse> {
        return try {
            val action = "update"
            val response = apiService.editProfilePenjual(action, id, fullName, telp, namaToko, deskripsi)
            Log.d(TAG, "Respons API editMenu: $response")

            if (response.isSuccessful) {
                ResultState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error dalam panggilan API: $errorBody")
                ResultState.Error("Error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getProfilePenjual(
        id: Int,
    ): ResultState<GetProfilePenjualResponse> {
        return try {
            val response = apiService.getProfilePenjual(id)
            Log.d(TAG, "Respons API editMenu: $response")

            if (response.isSuccessful) {
                ResultState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error dalam panggilan API: $errorBody")
                ResultState.Error("Error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun pesanMenu(
        idPembeli: Int,
        idPenjual: Int,
        idMenu: Int,
        noPesanan: String,
        qty: Int,
        catatan: String,
        jamPesan: String,
        jmlHarga: Int,
        status: Boolean,
    ): ResultState<PesanMenuResponse> {
        return try {
            val response = apiService.pesanMenu(idPembeli, idPenjual, idMenu, noPesanan, qty, catatan, jamPesan, jmlHarga, status)
            Log.d(TAG, "Respons API pesanMenu: $response")

            if (response.isSuccessful) {
                ResultState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error dalam panggilan API: $errorBody")
                ResultState.Error("Error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getHistoryPembeli(idPembeli: Int, year: Int, month: Int): ResultState<HistoryPembeliResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getHistoryPembeli(idPembeli, year, month)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        ResultState.Success(apiResponse)
                    } else {
                        ResultState.Error("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getHistoryPenjual(idPenjual: Int, year: Int, month: Int): ResultState<HistoryPenjualResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getHistoryPenjual(idPenjual, year, month)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        ResultState.Success(apiResponse)
                    } else {
                        ResultState.Error("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    ResultState.Error("Error: $errorBody")
                }
            } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}