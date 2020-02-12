package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanzu.kredit.R
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.helper.Yanza
import com.yanza.kredit.helper.saveToSharedPreference
import com.yanza.kredit.model.LoanModel
import java.text.DecimalFormat

class LoanLimitAsync(private var userId:Int) : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.YANZA_KREDIT + "getuserloanlimit/$userId"
            try {
                return HttpUtility.getRequest(url)
            } catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                val type = object : TypeToken<LoanModel>() {}.type
                val userModel = gson.fromJson<LoanModel>(result, type)
                try {
                    val formatter = DecimalFormat("#,###,###.##")
                    val amount = formatter.format(userModel.data?.amount)
                    Yanza.applicationContext.saveToSharedPreference(R.string.loan_limit, amount)
                }catch (e: Exception){
                    Log.e("INTEREESY",  e.message)
                }


            }
        }
    }
