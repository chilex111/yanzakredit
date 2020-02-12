package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class LoanModel(
    @SerializedName("data")
    val `data`: LoanData?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?
)


data class LoanData(
    @SerializedName("amount")
    val amount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("loan_id")
    val loanId: Int?
)