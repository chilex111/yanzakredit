package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class GenderModel(
    @SerializedName("data")
    val `data`: List<GenderData>?,
    @SerializedName("status")
    val status: Boolean?
)


data class GenderData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?
)