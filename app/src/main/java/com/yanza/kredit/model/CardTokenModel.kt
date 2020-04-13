package com.yanza.kredit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CardTokenModel (
    @SerializedName("card")
    var card: Card?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("status")
    var status: Boolean?
)
data class Card(
    @SerializedName("auth_code")
    var authCode: String?,
    @SerializedName("id")
    var id: Int?
)
