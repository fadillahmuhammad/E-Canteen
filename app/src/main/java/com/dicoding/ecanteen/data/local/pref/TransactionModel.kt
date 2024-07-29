package com.dicoding.ecanteen.data.local.pref

data class TransactionModel(
    val orderId: String,
    val menuId: Int,
    val nama: String,
    val gambar: String,
    val pembeliId: Int,
    val penjualId: Int,
    val quantity: Int,
    val catatan: String,
    val jam_pesan: String,
    val harga: Int,
    val jml_harga: Int,
    val status: Boolean,
)