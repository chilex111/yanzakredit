package com.yanza.kredit.apis

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.LoanRequestModel
import com.yanza.kredit.model.ResponseBooleanModel
import com.yanza.kredit.model.loan_model.BankModel
import fewchore.com.exolve.fewchore.model.CardModel
import fewchore.com.exolve.fewchore.model.FormModel
import org.json.JSONObject

class LoanRequestAsync(private var loanForm: FormModel?, private var cardModel: CardModel?,
                       private var bankModel: BankModel?, private var userId:Int,
                       var responseStringModel: MutableLiveData<LoanRequestModel>,
                       var errorString: MutableLiveData<String>): AsyncTask<Void, Int, String>() {
    override fun doInBackground(vararg p0: Void?): String? {

        val url = Const.YANZA_KREDIT +"requestloan"
        val map = JSONObject()

        map.put("duration_id", loanForm?.duration)
        map.put("user_id", userId)
        map.put("amount", loanForm?.loanAmount)
        map.put("interest", loanForm?.interest)
        map.put("interest_id", loanForm?.interest_id)
        map.put("card_id", cardModel?.cardId)
        map.put("userbank_id", bankModel?.acctID)
       // map.put("loan_totalpayback", loanForm?.totalPayback)
       // map.put("bank_name", bankModel?.bankName)
        map.put("accttype", bankModel?.acctType)
        map.put("accno", bankModel?.acctNo)
        try {
            return HttpUtility.postJsonAuth(url, map)
        } catch (e: Exception) {
        }
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()) {

            val gson = Gson()
            try {
                val type = object : TypeToken<LoanRequestModel>() {}.type
                val response = gson.fromJson<LoanRequestModel>(result, type)

                responseStringModel.value = response
            } catch (e: Exception) {
                if (e is IllegalStateException)
                    errorString.value = e.message
            }
        }
    }
}