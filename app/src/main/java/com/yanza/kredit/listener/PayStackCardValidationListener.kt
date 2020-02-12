package com.yanza.kredit.listener

import android.text.Editable
import com.yanza.kredit.enums.CardValidity


interface PayStackCardValidationListener {
    fun afterChange(cardValidity: CardValidity, editable: Editable)

    fun paramIsValid(z: Boolean, cardValidity: CardValidity)
}
