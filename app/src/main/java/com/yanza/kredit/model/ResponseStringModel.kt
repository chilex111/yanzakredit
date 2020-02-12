package com.yanza.kredit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseStringModel {

    @Expose
    var msg: String? = null
    @Expose
    var error_msg: String? = null
    @Expose
    var status: String? = null
    @Expose
    var loan_id: String? = null
    @Expose
    var name: String? = null
    @Expose
    var user_status: String? = null

}


data class ResponseBooleanModel(
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?
)
