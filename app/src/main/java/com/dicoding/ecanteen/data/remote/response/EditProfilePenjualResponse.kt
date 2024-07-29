package com.dicoding.ecanteen.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditProfilePenjualResponse(

	@field:SerializedName("profile")
	val profile: DataProfilePenjual? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataProfilePenjual(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("telp")
	val telp: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("nama_toko")
	val namaToko: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
