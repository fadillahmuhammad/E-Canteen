package com.dicoding.ecanteen.data.local.pref

data class ProfilePembeliModel(
    val id: Int,
    val fullName: String,
    val email: String,
    val telp: String,
)

data class ProfilePenjualModel(
    val id: Int,
    val fullName: String,
    val email: String,
    val telp: String,
    val namaToko: String,
    val deskripsi: String,
)