package com.yanza.kredit.model

import com.google.gson.annotations.SerializedName

class CardDetails0 {

    @SerializedName("card_authorization_code")
    var cardAuthorizationCode: String? = null
    @SerializedName("card_card_type")
    var cardCardType: String? = null
    @SerializedName("card_cvv")
    var cardCvv: String? = null
    @SerializedName("card_id")
    var cardId: String? = null
    @SerializedName("card_expiry_month")
    var cardExpiryMonth: String? = null
    @SerializedName("card_expiry_year")
    var cardExpiryYear: String? = null
    @SerializedName("card_number")
    var cardNumber: String? = null
    @SerializedName("card_reference")
    var cardReference: String? = null
    @SerializedName("card_user_id")
    var cardUserId: String? = null

}
class Register(
    var firstName:String,
    var surname: String,
    var email:String,
    var address: String,
    var office_address: String,
    var dob: String,
    var password: String,
    var phone: String,
    var gender: Int,
    var photo: String
)