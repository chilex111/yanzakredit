package com.yanza.kredit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AcctListModel (
    @SerializedName("data")
    val `data`: List<AcctListData>?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("msg")
    val msg: Boolean?
    )

    data class AcctListData(
        @SerializedName("accno")
        val accno: String?,
        @SerializedName("accttype")
        val accttype: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("id")
        val id: Int?
    )
