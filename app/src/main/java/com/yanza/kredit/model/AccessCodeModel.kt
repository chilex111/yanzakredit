package com.yanza.kredit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AccessCodeModel (
    @SerializedName("data")
    val `data`: AccessData?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?
)




class AccessData (

    @SerializedName("access_code")
    val accessCode: String?,
    @SerializedName("reference")
    val reference: String?,
    @SerializedName("user_id")
    val userId: Int?

)
class AcctModel (
    @SerializedName("data")
    val `data`: AcctData1?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?
)


class AcctData1 (
    @SerializedName("userbank_id")
    val userbank_id: Int?

)


