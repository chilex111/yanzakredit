package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.ResponseBooleanModel
import com.yanza.kredit.model.ResponseStringModel
import org.json.JSONObject

class PayNowAsync
    (
    private val userId: Int,
    val cardId: String?,
    val loanId: Int?,
    val amountToPay: String?,
    val responseModel: MutableLiveData<ResponseBooleanModel>,
    val errorString: MutableLiveData<String>
) : AsyncTask<Void, Int, String>() {
    override fun doInBackground(vararg p0: Void?): String {
        val url: String
        val map = JSONObject()
        map.put("card_id", cardId)
        map.put("user_id", userId)
        map.put("loan_id", loanId)
        if (amountToPay.isNullOrEmpty())
            url = Const.YANZA_KREDIT + "paynow"
        else {
            url = Const.YANZA_KREDIT + "payinstallment"
            map.put("amount", amountToPay)
        }

        return HttpUtility.postJsonAuth(url, map)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()) {
            val gson = Gson()
            try {
                if(result.contains("msg")){
                val type = object : TypeToken<ResponseBooleanModel>() {}.type
                val response = gson.fromJson<ResponseBooleanModel>(result, type)
                responseModel.value = response
                }
                else{
                    errorString.value = result
                }
                Log.e("AMOUNT_RESPONSE", result.toString())
            } catch (e: Exception) {
                errorString.value = e.message.toString()
            }
        }
    }

}