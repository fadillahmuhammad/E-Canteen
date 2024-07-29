package com.dicoding.ecanteen.data.local.pref

data class UserModel(
    val id: Int,
    val message: String,
    val token: String,
    val fullName: String,
    val telp: String,
    val username: String,
    val isLogin: Boolean = false,
    val storeName: String,
    val desc: String,
    val userType: String
)