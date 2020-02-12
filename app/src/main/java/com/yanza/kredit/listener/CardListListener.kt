package com.yanza.kredit.listener

import com.yanza.kredit.model.CardDetails

interface CardListListener {
    fun cardDetailsListener(cardDetails: MutableList<CardDetails>?, msg: String)
}