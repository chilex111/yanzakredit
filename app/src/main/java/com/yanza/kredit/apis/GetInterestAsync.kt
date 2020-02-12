package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.helper.Yanza
import com.yanza.kredit.helper.saveToSharedPreference
import com.yanza.kredit.model.Interest
import com.yanzu.kredit.R

class GetInterestAsync : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.YANZA_KREDIT + "interest"
            try {
                return HttpUtility.getRequestNoToken(url)
            } catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                val type = object : TypeToken<Interest>() {}.type
                val userModel = gson.fromJson<Interest>(result, type)
                try {
                    Yanza.applicationContext.saveToSharedPreference(R.string.interest, "${userModel.data?.rate!!}:${userModel.data.id}")
                }catch (e: Exception){
                    Log.e("INTEREESY",  e.message)
                }


            }
        }
    }
