package com.yanza.kredit.viwModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.yanza.kredit.model.*
import com.yanza.kredit.model.LoanModel
import com.yanza.kredit.model.DurationModel
import com.yanza.kredit.model.loan_model.BankModel
import fewchore.com.exolve.fewchore.model.CardModel
import fewchore.com.exolve.fewchore.model.FormModel

class MainViewModel : ViewModel() {
    private val repository:Repository = Repository()


    fun addCard(userId: Int): LiveData<AccessCodeModel>{
        return repository.getAccessCode(userId)
    }

    fun verifyOnServer(reference: String): LiveData<CardTokenModel>{
        return repository.verifyOnServer(reference)
    }
    fun cardList(userId: Int): LiveData<CardListModel>{
        return repository.cardList(userId)
    }

    fun bankList(userId: Int): LiveData<AcctListModel> {
        return repository.accountList(userId)
    }
    fun verifyBVN(bvn: String, userId: Int): LiveData<BVNModel>{
        return repository.verifyBVN(bvn, userId)
    }

    fun banks(): LiveData<BankListModel>{
        return repository.banks()
    }

    fun accountName(acctNo: String, bankSelected: String?): MutableLiveData<AcctNameModel>{
        return repository.acctName(acctNo, bankSelected)
    }

    fun history(userId: Int):MutableLiveData<HistoryModel>{
       return repository.loanHistory(userId)

    }

    fun error():LiveData<String> {
        return repository.error()
    }

    fun payLoan(
        userId: Int,
        cardId: String?,
        loanId: Int,
        amountToPay: String?
    ):LiveData<ResponseBooleanModel> {
        return repository.payLoan(userId, cardId, loanId, amountToPay)
    }

    fun loanDetail(userId:Int):LiveData<LoanModel> {
        return repository.loanDetail(userId)

    }

    fun addAcct(acctNo: String, bankId: String, userId: Int, acctType: String):LiveData<AcctModel> {
        return repository.addAcct(acctNo, bankId, acctType, userId)

    }

    fun requestLoan(loanModel: FormModel?, cardInfo: CardModel?, bankInfo: BankModel?, userId: Int):LiveData<LoanRequestModel> {
        return repository.requestLoan(loanModel,cardInfo,bankInfo, userId)
    }

    fun updateProfile(profile: Register):LiveData<ResponseBooleanModel> {
        return repository.updateProfile(profile)
    }

    fun key():LiveData<KeyModel> {
        return repository.keys()
    }

    fun deletLoan(userId: Int, loanId: Int):LiveData<ResponseBooleanModel> {
        return repository.deleteLoan(userId, loanId)
    }

    fun duration():LiveData<DurationModel> {
        return repository.duration()
    }
}
