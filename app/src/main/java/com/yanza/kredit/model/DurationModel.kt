package com.yanza.kredit.model

import com.google.gson.annotations.SerializedName

data class DurationModel(

    @SerializedName("data")
    var `data`: List<DurationData>,
    @SerializedName("status")
    var status: Boolean
)


data class DurationData(
    @SerializedName("id")
    var id: Int,
    @SerializedName("value")
    var value: Int
)