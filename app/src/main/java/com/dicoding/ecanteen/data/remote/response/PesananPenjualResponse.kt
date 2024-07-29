package com.dicoding.ecanteen.data.remote.response

import com.google.gson.annotations.SerializedName

data class PesananPenjualResponse(

	@field:SerializedName("data_pesanan_penjual")
	val dataPesananPenjual: List<DataPesananPenjualItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataPesananPenjualItem(

	@field:SerializedName("id_menu")
	val idMenu: String? = null,

	@field:SerializedName("telp")
	val telp: String? = null,

	@field:SerializedName("jam_pesan")
	val jamPesan: String? = null,

	@field:SerializedName("jml_harga")
	val jmlHarga: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("stok")
	val stok: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("id_penjual")
	val idPenjual: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("no_pesanan")
	val noPesanan: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("id_pembeli")
	val idPembeli: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
