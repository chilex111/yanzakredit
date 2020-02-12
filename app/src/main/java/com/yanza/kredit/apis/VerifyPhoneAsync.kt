package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.SignupModel
import org.json.JSONObject

class VerifyPhoneAsync(
    private val code: String,
    var userId: Int,
    var signUpModel: MutableLiveData<SignupModel>,
    var errorString: MutableLiveData<String>
) : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.YANZA_KREDIT + "verifytoken"
            val map = JSONObject()
            map.put("otp", code)
            map.put("user_id",userId)
            return HttpUtility.postJsonAuth(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {

                try{
                    val responseType = object : TypeToken<SignupModel>() {}.type
                    val response = Gson().fromJson<SignupModel>(result, responseType)
                    signUpModel.value = response
                }catch (e: Exception){
                    if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                    errorString.value = e.message
                }
            }
            else   errorString.value = "return is empty"

        }
    }
