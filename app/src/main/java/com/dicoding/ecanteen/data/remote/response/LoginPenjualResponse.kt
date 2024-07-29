package com.dicoding.ecanteen.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginPenjualResponse(
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("token")
    val token: String? = null,
    @field:SerializedName("payload")
    val payload: PayloadPenjual? = null
)

data class PayloadPenjual(
    @field:SerializedName("id")
    val id: Int? = null,
    @field:SerializedName("nama_toko")
    val storeName: String? = null,
    @field:SerializedName("nama_lengkap")
    val fullName: String? = null,
    @field:SerializedName("deskripsi")
    val desc: String? = null,
    @field:SerializedName("telp")
    val telp: String? = null,
    @field:SerializedName("username")
    val username: String? = null
)