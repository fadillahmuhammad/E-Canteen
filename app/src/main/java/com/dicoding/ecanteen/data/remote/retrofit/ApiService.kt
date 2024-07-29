package com.dicoding.ecanteen.data.remote.retrofit

import com.dicoding.ecanteen.data.local.pref.MenuModel
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("ecanteen/login_pembeli.php")
    suspend fun loginPembeli(
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginPembeliResponse

    @FormUrlEncoded
    @POST("ecanteen/login_penjual.php")
    suspend fun loginPenjual(
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginPenjualResponse

    @FormUrlEncoded
    @POST("ecanteen/register_pembeli.php")
    suspend fun registerPembeli(
        @Field("nama_lengkap") fullName: String,
        @Field("telp") telp: String,
        @Field("id_kartu") idKartu: String,
        @Field("tipe_user") tipeUser: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("ecanteen/register_penjual.php")
    suspend fun registerPenjual(
        @Field("nama_toko") storeName: String,
        @Field("nama_lengkap") fullName: String,
        @Field("telp") telp: String,
        @Field("id_kartu") idKartu: String,
        @Field("tipe_user") tipeUser: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): RegisterResponse

    @GET("ecanteen/menu_crud.php")
    suspend fun getMenus(): List<MenuModel>

    @POST("ecanteen/menu_crud.php")
    suspend fun getMenusFromId(
        @Query("id") id: Int
    ): List<MenuModel>

    @FormUrlEncoded
    @POST("ecanteen/menu_crud.php")
    suspend fun getMenuById(
        @Field("menu_id") id: Int
    ): AddMenuResponse

    @FormUrlEncoded
    @POST("ecanteen/menu_crud.php")
    suspend fun deleteMenuFromId(
        @Field("action") action: String = "delete",
        @Field("id") id: Int
    ): Response<Unit>

    @Multipart
    @POST("ecanteen/menu_crud.php")
    suspend fun addMenu(
        @Part("id_penjual") idPenjual: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part gambar: MultipartBody.Part
    ): AddMenuResponse

    @Multipart
    @POST("ecanteen/menu_crud.php")
    suspend fun editMenu(
        @Part("action") action: RequestBody,
        @Part("id") id: RequestBody,
        @Part("id_penjual") idPenjual: RequestBody,
        @Part("nama") name: RequestBody,
        @Part("deskripsi") description: RequestBody,
        @Part("harga") price: RequestBody,
        @Part("stok") stock: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Response<AddMenuResponse>

    @FormUrlEncoded
    @POST("ecanteen/saldo.php")
    suspend fun getSaldo(
        @Field("id_user") idUser: Int,
        @Field("tipe_user") tipeUser: String
    ): Response<SaldoResponse>

    @FormUrlEncoded
    @POST("ecanteen/transaksi_saldo.php")
    suspend fun getTransaksiSaldo(
        @Field("id_user") idUser: Int,
        @Field("tipe_user") tipeUser: String
    ): Response<TransaksiSaldoResponse>

    @FormUrlEncoded
    @POST("ecanteen/top_up_saldo.php")
    suspend fun topUpSaldo(
        @Field("id_user") idUser: Int,
        @Field("tipe_user") tipeUser: String,
        @Field("id_saldo") idSaldo: Int,
        @Field("amount") amount: Int
    ): Response<TopUpResponse>

    @FormUrlEncoded
    @POST("ecanteen/pesanan_pembeli.php")
    suspend fun getPesananPembeli(
        @Field("id_pembeli") idPembeli: Int,
    ): Response<PesananPembeliResponse>

    @FormUrlEncoded
    @POST("ecanteen/pesanan_penjual.php")
    suspend fun getPesananPenjual(
        @Field("id_penjual") idPenjual: Int,
    ): Response<PesananPenjualResponse>

    @FormUrlEncoded
    @POST("ecanteen/pesanan_pembeli.php")
    suspend fun getDetailPesananPembeli(
        @Field("id_pesanan_pembeli") idPesananPembeli: Int,
    ): Response<DetailPesananPembeliResponse>

    @FormUrlEncoded
    @POST("ecanteen/pesanan_penjual.php")
    suspend fun getDetailPesananPenjual(
        @Field("id_pesanan_penjual") idPesananPenjual: Int,
    ): Response<DetailPesananPenjualResponse>

    @FormUrlEncoded
    @POST("ecanteen/pesanan_penjual.php")
    suspend fun editStatusPesanan(
        @Field("id") id: Int,
        @Field("status_bool") statusBool: Int,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("ecanteen/pesanan_pembeli.php")
    suspend fun deletePesananFromId(
        @Field("action") action: String = "delete",
        @Field("id") id: Int
    ): Response<Unit>

    @FormUrlEncoded
    @POST("ecanteen/edit_profile_pembeli.php")
    suspend fun editProfilePembeli(
        @Field("action") action: String,
        @Field("id") id: Int,
        @Field("nama_lengkap") fullName: String,
        @Field("telp") telp: String,
    ): Response<EditProfilePembeliResponse>

    @FormUrlEncoded
    @POST("ecanteen/get_profile_pembeli.php")
    suspend fun getProfilePembeli(
        @Field("id") id: Int,
    ): Response<GetProfilePembeliResponse>

    @FormUrlEncoded
    @POST("ecanteen/edit_profile_penjual.php")
    suspend fun editProfilePenjual(
        @Field("action") action: String,
        @Field("id") id: Int,
        @Field("nama_lengkap") fullName: String,
        @Field("telp") telp: String,
        @Field("nama_toko") namaToko: String,
        @Field("deskripsi") deskripsi: String,
    ): Response<EditProfilePenjualResponse>

    @FormUrlEncoded
    @POST("ecanteen/get_profile_penjual.php")
    suspend fun getProfilePenjual(
        @Field("id") id: Int,
    ): Response<GetProfilePenjualResponse>

    @FormUrlEncoded
    @POST("ecanteen/insert_pesanan.php")
    suspend fun pesanMenu(
        @Field("id_pembeli") idPembeli: Int,
        @Field("id_penjual") idPenjual: Int,
        @Field("id_menu") idMenu: Int,
        @Field("no_pesanan") noPesanan: String,
        @Field("qty") qty: Int,
        @Field("catatan") catatan: String,
        @Field("jam_pesan") jamPesan: String,
        @Field("jml_harga") jmlHarga: Int,
        @Field("status") status: Boolean,
    ): Response<PesanMenuResponse>

    @FormUrlEncoded
    @POST("ecanteen/get_history_pembeli.php")
    suspend fun getHistoryPembeli(
        @Field("id_pembeli") idPembeli: Int,
        @Field("year") year: Int,
        @Field("month") month: Int,
    ): Response<HistoryPembeliResponse>

    @FormUrlEncoded
    @POST("ecanteen/get_history_penjual.php")
    suspend fun getHistoryPenjual(
        @Field("id_penjual") idPenjual: Int,
        @Field("year") year: Int,
        @Field("month") month: Int,
    ): Response<HistoryPenjualResponse>
}