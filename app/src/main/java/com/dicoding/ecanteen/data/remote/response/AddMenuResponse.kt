package com.dicoding.ecanteen.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddMenuResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("menu")
    val menu: Menu? = null
)

data class Menu(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("harga")
    val harga: String? = null,

    @field:SerializedName("id_penjual")
    val idPenjual: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("deskripsi")
    val deskripsi: String? = null,

    @field:SerializedName("stok")
    val stok: String? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null
)
