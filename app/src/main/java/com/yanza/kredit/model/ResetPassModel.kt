package com.yanza.kredit.model

import com.google.gson.annotations.SerializedName

class ResetPassModel(
    @SerializedName("data")
    val `data`: ForgotData?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("msg")
    val msg: String?
)

data class ForgotData(
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?
)