package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.*
import com.yanza.kredit.model.GenderModel
import com.yanza.kredit.model.Interest
import com.yanzu.kredit.R

class GenderAsync : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.YANZA_KREDIT + "gender"
            try {
                return HttpUtility.getRequestNoHeader(url)
            } catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                val type = object : TypeToken<GenderModel>() {}.type
                val gender = gson.fromJson<GenderModel>(result, type)
                try {
                    Yanza.applicationContext.saveValueToSharedPrefs(R.string.array_gender, gender.data!!)
                }catch (e: Exception){
                    Log.e("INTEREESY",  e.message.toString())
                }


            }
        }
    }
