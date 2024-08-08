package com.mtz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("error")
    val error: BaseApiErrorDto?,
    @SerializedName("quotes")
    val quotes: Map<String, Double>?
)

data class BaseApiErrorDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("info")
    val info: String,

    )