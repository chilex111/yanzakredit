package com.yanza.kredit.apis

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.CardTokenModel
import org.json.JSONObject
import java.lang.Exception

class VerifyOnServer(
         private var reference: String?,
         private val accessCode: MutableLiveData<CardTokenModel>,
         var errorString: MutableLiveData<String>
     ): AsyncTask<Void, Int, String>() {

        override fun doInBackground(vararg p0: Void?): String {
            val map = JSONObject()
            map.put("reference", reference)
            val url = Const.YANZA_KREDIT +"verifycode"
            return HttpUtility.postJsonAuth(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val gson = Gson()
                val type = object : TypeToken<CardTokenModel>() {}.type
                val cardTokenModel = gson.fromJson<CardTokenModel>(result, type)
                try{
                    if (cardTokenModel.status!!){
                        accessCode.value = cardTokenModel

                    }
                }catch (e:Exception){
                    errorString.value = e.message
                }

            }
            else
                errorString.value = "return is empty"
        }

    }