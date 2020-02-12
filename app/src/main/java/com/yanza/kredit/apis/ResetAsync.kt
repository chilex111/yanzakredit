package com.yanza.kredit.apis

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.ResetPassModel
import com.yanza.kredit.model.ResponseStringModel
import org.json.JSONObject

class ResetAsync(
    private val codeTExt: String,
    private val passText:
    String,
    var responseModel: MutableLiveData<ResetPassModel>,
    var errorString: MutableLiveData<String>
): AsyncTask<Void, Int, String>()
    {


        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.YANZA_KREDIT + "resetpassword"
            val map = JSONObject()
            map.put("password", passText)
            map.put("otp", codeTExt)

            return HttpUtility.postJson(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                try {
                val type = object : TypeToken<ResetPassModel>() {}.type
                val response = gson.fromJson<ResetPassModel>(result, type)
                    responseModel.value = response
            }catch (e: Exception){
                    errorString.value = e.message
                }
            }
        }

    }