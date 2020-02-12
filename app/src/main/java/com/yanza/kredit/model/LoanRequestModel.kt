package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class LoanRequestModel(
    @SerializedName("data")
    val `data`: LoanRequestData?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?
)
data class LoanRequestData(
    @SerializedName("loan_id")
    val loanId: Int?
)