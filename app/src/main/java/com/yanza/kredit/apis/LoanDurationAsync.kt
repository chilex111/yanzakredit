package com.yanza.kredit.apis

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanzu.kredit.R
import com.yanza.kredit.model.LoanTimModel
import com.yanza.kredit.helper.saveValueToSharedPrefs

class LoanDurationAsync( private var context: Context) : AsyncTask<Void, Int, String>() {
    override fun doInBackground(vararg p0: Void?): String {
        val url = Const.YANZA_KREDIT+"get/loanfrequency"
        return HttpUtility.getRequest(url)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type= object : TypeToken<List<LoanTimModel>>(){}.type
                val activeLoan = gson.fromJson<List<LoanTimModel>>(result, type)
                if (activeLoan != null){
                    context.saveValueToSharedPrefs(R.string.loan_duration, activeLoan)
                }
        }catch (e: Exception){
            Log.e("TAG", e.message.toString())
            }
        }
    }

}