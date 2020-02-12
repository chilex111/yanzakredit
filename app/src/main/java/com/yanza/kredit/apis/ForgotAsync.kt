package com.yanza.kredit.apis

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.ResetPassModel
import org.json.JSONObject

class ForgotAsync(
    private val valueEntered: String,
    var resetModel: MutableLiveData<ResetPassModel>,
    var errorString: MutableLiveData<String>
): AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.YANZA_KREDIT + "forgotpassword"
            val map = JSONObject()
            map.put("email", valueEntered)


            return HttpUtility.postJson(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){

                val gson = Gson()
                try{
                val type = object : TypeToken<ResetPassModel>() {}.type
                val resetPassModel = gson.fromJson<ResetPassModel>(result, type)
                    resetModel.value = resetPassModel

            }catch (e: Exception) {
                    if (e is IllegalStateException) {
                        errorString.value = e.message
                    }
                }
            }
        }

    }
