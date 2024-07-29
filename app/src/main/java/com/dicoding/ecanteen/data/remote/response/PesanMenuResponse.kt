package com.dicoding.ecanteen.data.remote.response

import com.google.gson.annotations.SerializedName

data class PesanMenuResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
