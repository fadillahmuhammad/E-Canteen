package com.dicoding.ecanteen.data.local.pref

data class MenuModel(
    val id: Int,
    val message: String,
    val id_penjual: Int,
    val nama: String,
    val deskripsi: String,
    val harga: Int,
    val stok: Int,
    val gambar: String,
    val nama_toko: String,
    val nama_lengkap: String,
    val penjual_deskripsi: String,
    val penjual_telp: String
)