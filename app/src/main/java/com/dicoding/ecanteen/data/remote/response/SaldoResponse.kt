package com.dicoding.ecanteen.data.remote.response

import com.google.gson.annotations.SerializedName

data class SaldoResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("nama_pemilik")
	val namaPemilik: String? = null,

	@field:SerializedName("id_kartu")
	val idKartu: String? = null,

	@field:SerializedName("jumlah_saldo")
	val jumlahSaldo: Int? = null
)
