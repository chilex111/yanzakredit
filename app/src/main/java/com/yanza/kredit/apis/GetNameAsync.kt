package com.yanza.kredit.apis

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.AcctNameModel
import com.yanza.kredit.model.ResponseStringModel
import org.json.JSONObject

class GetNameAsync(
    private var acctNo: String,
    var bankSelected: String?,
    var responseModel: MutableLiveData<AcctNameModel>,
    var errorString: MutableLiveData<String>
): AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.YANZA_KREDIT + "accountnumber?name=$bankSelected&number=$acctNo"
            val map = JSONObject()

            map.put("account_number", acctNo)
            map.put("bank_name", bankSelected)
            try {
                return HttpUtility.getRequestNoToken(url)
            } catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                val type = object : TypeToken<AcctNameModel>() {}.type
                val userModel = gson.fromJson<AcctNameModel>(result, type)
                try {
                    responseModel.value = userModel
                }catch (e: Exception){
                    errorString.value = e.message
                }


            }
        }
    }
