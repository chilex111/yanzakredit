package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class AcctNameModel(
    @SerializedName("data")
    val data: AcctData,
    @SerializedName("status")
    val status: Boolean
)

data class AcctData(
    @SerializedName("name")
    val name: String?,
    @SerializedName("number")
    val number: String?
)