package com.yanza.kredit.listener

import com.yanza.kredit.enums.NavigationDirection
import com.yanza.kredit.model.loan_model.BankModel
import fewchore.com.exolve.fewchore.model.CardModel
import fewchore.com.exolve.fewchore.model.FormModel


interface FragmentListener {
    fun onFragmentNavigation(navigationDirection: NavigationDirection)

    fun onFormDetailSubmit(formModel: FormModel)

    fun onBankDetailSubmit(bankModel: BankModel)

    fun onCardDetailSubmit(cardModel: CardModel)

   // fun onLookUp()

  //  fun onPasswordDetailSubmit(passwordModel: PasswordModel)

}
