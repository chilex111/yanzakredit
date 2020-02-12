package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.LoanModel

class ActiveLoanAsync(
    private var userId: Int,
    var loanModel: MutableLiveData<LoanModel>,
    var errorString: MutableLiveData<String>
): AsyncTask<Void, Int, String>(){

    override fun doInBackground(vararg p0: Void?): String? {

        val url = Const.YANZA_KREDIT + "userloanstatus/" + userId
        try {
            return HttpUtility.getRequest(url)
        } catch (e: Exception) {
        }
        return null
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type= object : TypeToken<LoanModel>(){}.type
                val activeLoan = gson.fromJson<LoanModel>(result, type)
                    loanModel.value = activeLoan

            }catch (e: Exception){
                if (e is IllegalStateException)
                    errorString.value = e.message
            }
        }
    }
}