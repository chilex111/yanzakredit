package com.yanza.kredit.viwModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel;
import com.yanza.kredit.model.*

class AuthenticationViewModel : ViewModel() {
    private val repository:Repository = Repository()

    fun verifyBVN(bvn: String, userId: Int): LiveData<BVNModel>{
        return repository.verifyBVN(bvn, userId)
    }

    fun register(register: Register):LiveData<SignupModel> {
        return repository.register(register)
    }

    fun confirmOTP(code: String, userId: Int):LiveData<SignupModel> {
        return repository.confirmOTP(code,userId)
    }

    fun error():LiveData<String>{
        return repository.error()
    }

    fun login(email: String, password: String):LiveData<LoginModel> {
        return repository.login(email, password)
    }

    fun reset(codeTExt: String, passText: String):LiveData<ResetPassModel> {
        return repository.reset(codeTExt, passText)
    }

    fun forgot(valueEntered: String):LiveData<ResetPassModel> {
        return  repository.forgot(valueEntered)
    }
}
