package com.yanza.kredit.viwModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanza.kredit.apis.*
import com.yanza.kredit.model.*
import com.yanza.kredit.model.DurationModel
import com.yanza.kredit.model.loan_model.BankModel
import fewchore.com.exolve.fewchore.model.CardModel
import fewchore.com.exolve.fewchore.model.FormModel

class Repository {

    var errorString: MutableLiveData<String> = MutableLiveData()

    var accessCode: MutableLiveData<AccessCodeModel> = MutableLiveData()
    var cardTokenModel: MutableLiveData<CardTokenModel> = MutableLiveData()
    var cardListModel: MutableLiveData<CardListModel> = MutableLiveData()
    var responseModel: MutableLiveData<ResponseStringModel> = MutableLiveData()
    var bvnModel: MutableLiveData<BVNModel> = MutableLiveData()
    var responseBooleanModel: MutableLiveData<ResponseBooleanModel> = MutableLiveData()
    var acctNameLiveData: MutableLiveData<AcctNameModel> = MutableLiveData()
    var signUpModel: MutableLiveData<SignupModel> = MutableLiveData()
    var loginModel: MutableLiveData<LoginModel> = MutableLiveData()
    var bankAcctModel: MutableLiveData<AcctListModel> = MutableLiveData()
    var bankListModel: MutableLiveData<BankListModel> = MutableLiveData()
    var historyModel: MutableLiveData<HistoryModel> = MutableLiveData()
    var acctModel: MutableLiveData<AcctModel> = MutableLiveData()
    var loanModel: MutableLiveData<LoanModel> = MutableLiveData()
    var loanRequestModel: MutableLiveData<LoanRequestModel> = MutableLiveData()
    var keyModel: MutableLiveData<KeyModel> = MutableLiveData()
    var resetModel: MutableLiveData<ResetPassModel> = MutableLiveData()
    var durationModel: MutableLiveData<DurationModel> = MutableLiveData()

    fun verifyBVN(bvn: String?, userId: Int): LiveData<BVNModel> {
        VerifyBVNAsync(bvn!!, userId, bvnModel, errorString).execute()
        return bvnModel
    }

    fun register(register: Register): LiveData<SignupModel> {
        SignUpAsync(register, signUpModel, errorString).execute()
        return signUpModel
    }
    fun confirmOTP(code: String, userId: Int): LiveData<SignupModel> {
        VerifyPhoneAsync(code, userId,signUpModel, errorString).execute()
        return signUpModel
    }

    fun getAccessCode(id: Int): LiveData<AccessCodeModel> {
        AddCardAsync(id, accessCode, errorString).execute()
        return accessCode
    }

    fun verifyOnServer(reference: String?): LiveData<CardTokenModel> {
        VerifyOnServer(reference!!, cardTokenModel, errorString).execute()
        return cardTokenModel
    }
    fun cardList(userID: Int): LiveData<CardListModel> {
        CardListAsync(userID, cardListModel, errorString).execute()
        return cardListModel
    }

    fun error(): LiveData<String> {
        return errorString
    }

    fun login(email: String, password: String): LiveData<LoginModel> {
        LoginAsync(email, password, loginModel, errorString).execute()
        return loginModel
    }

    fun accountList(userId: Int): LiveData<AcctListModel> {
        AcctListAsync(userId, bankAcctModel, errorString).execute()
        return bankAcctModel
    }

    fun banks(): MutableLiveData<BankListModel> {
        BankListAsync(bankListModel, errorString).execute()
        return bankListModel
    }

    fun acctName(acctNo: String, bankSelected: String?): MutableLiveData<AcctNameModel> {
        GetNameAsync(acctNo, bankSelected, acctNameLiveData, errorString).execute()
        return acctNameLiveData
    }

    fun loanHistory(userId: Int):MutableLiveData<HistoryModel> {
        HistoryAsnc(userId, historyModel, errorString).execute()
        return historyModel
    }

    fun payLoan(
        userId: Int,
        cardId: String?,
        loanId: Int,
        amountToPay: String?
    ): LiveData<ResponseBooleanModel> {
        PayNowAsync(userId, cardId, loanId, amountToPay,responseBooleanModel, errorString).execute()
        return responseBooleanModel
    }

    fun addAcct(acctNo: String, bankId: String, acctType: String, userId: Int): LiveData<AcctModel> {
        AddAccountAsync(acctNo, bankId, acctType, userId, acctModel, errorString).execute()
        return acctModel
    }

    fun loanDetail(userId: Int): LiveData<LoanModel> {
        ActiveLoanAsync(userId, loanModel, errorString).execute()
        return loanModel
    }

    fun requestLoan(loanModel: FormModel?, cardInfo: CardModel?, bankInfo: BankModel?, userId: Int): LiveData<LoanRequestModel> {
        LoanRequestAsync(loanModel,cardInfo,bankInfo,userId,loanRequestModel, errorString).execute()
        return loanRequestModel
    }

    fun updateProfile(profile: Register): LiveData<ResponseBooleanModel> {

        return responseBooleanModel
    }

    fun reset(codeTExt: String, passText: String): LiveData<ResetPassModel> {
        ResetAsync(codeTExt, passText, resetModel, errorString).execute()
        return resetModel
    }

    fun forgot(valueEntered: String): LiveData<ResetPassModel> {
        ForgotAsync(valueEntered, resetModel, errorString).execute()
        return resetModel
    }


    fun keys(): LiveData<KeyModel> {
        KeyAsync(keyModel).execute()
        return keyModel
    }

    fun deleteLoan(userId: Int, loanId: Int?):LiveData<ResponseBooleanModel> {
        DeleteAsync(userId, loanId, responseBooleanModel, errorString).execute()
        return responseBooleanModel
    }

    fun duration(): LiveData<DurationModel> {
        DurationAsync(durationModel, errorString).execute()
        return durationModel
    }


}