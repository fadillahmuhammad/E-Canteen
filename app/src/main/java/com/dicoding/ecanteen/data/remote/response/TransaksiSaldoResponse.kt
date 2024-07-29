package com.dicoding.ecanteen.data.remote.response

import com.google.gson.annotations.SerializedName

data class TransaksiSaldoResponse(

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)

data class DataItem(

	@field:SerializedName("jenis_transaksi")
	val jenisTransaksi: String? = null,

	@field:SerializedName("jumlah_transaksi")
	val jumlahTransaksi: Int? = null,

	@field:SerializedName("id_menu")
	val idMenu: Any? = null,

	@field:SerializedName("tanggal_transaksi")
	val tanggalTransaksi: String? = null,

	@field:SerializedName("id_penjual")
	val idPenjual: Any? = null,

	@field:SerializedName("nama")
	val nama: Any? = null,

	@field:SerializedName("gambar")
	val gambar: Any? = null
)
