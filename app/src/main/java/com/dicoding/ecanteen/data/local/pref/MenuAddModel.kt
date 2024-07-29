package com.dicoding.ecanteen.data.local.pref

data class MenuAddModel(
    val id: String,
    val message: String,
    val id_penjual: String,
    val nama: String,
    val deskripsi: String,
    val harga: String,
    val stok: String,
    val gambar: String,
    val nama_toko: String,
    val nama_lengkap: String,
    val penjual_deskripsi: String,
    val penjual_telp: String,
)