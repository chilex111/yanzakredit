package com.yanza.kredit.apis

import android.os.AsyncTask
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
    val responseModel: MutableLiveData<ResponseBooleanModel>,
    val errorString: MutableLiveData<String>
): AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.YANZA_KREDIT+"paynow"

            val map = JSONObject()
            map.put("card_id", cardId)
            map.put("user_id", userId)
            map.put("loan_id", loanId)
            return HttpUtility.postJsonAuth(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val gson = Gson()
                try {
                    val type = object : TypeToken<ResponseBooleanModel>() {}.type
                    val response = gson.fromJson<ResponseBooleanModel>(result, type)
                    responseModel.value = response

                }catch (e:Exception){
                    errorString.value = e.message.toString()
                }
            }
        }

    }