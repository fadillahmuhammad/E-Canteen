package com.dicoding.ecanteen.data.remote.response
import com.google.gson.annotations.SerializedName

data class TopUpResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)
