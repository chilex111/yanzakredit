package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class KeyModel(
    @SerializedName("public_key")
    val publicKey: String?,
    @SerializedName("secret_key")
    val secretKey: String?
)