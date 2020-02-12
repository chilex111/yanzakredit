package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.AcctModel
import com.yanza.kredit.model.ResponseStringModel
import org.json.JSONObject

class AddAccountAsync(
   private var acctNo: String, private var bankId: String, private var acctType: String, private var userId: Int,
    var responseModel: MutableLiveData<AcctModel>,
    var errorString: MutableLiveData<String>
):
            AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.YANZA_KREDIT +"addaccount"
            val map = JSONObject()
            map.put("user_id", userId)
            map.put("bank_id", bankId)
            map.put("accno", acctNo)
            map.put("accttype", acctType)
            return HttpUtility.postJsonAuth(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                try{
                    val type = object : TypeToken<AcctModel>() {}.type
                    val userModel = gson.fromJson<AcctModel>(result, type)
                    responseModel.value = userModel

                }catch (e: Exception){
                    if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                    errorString.value = "return is empty"
                }
            }
        }
    }