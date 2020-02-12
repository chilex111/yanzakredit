package com.yanza.kredit.listener

interface StringListener {
    fun accountDetailsListener(cardNo: String?, cardExpiry: String?, cardId: String?, s: String?, auth_code: String?)
}
