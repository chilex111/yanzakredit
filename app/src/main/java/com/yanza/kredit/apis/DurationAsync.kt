package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.*
import com.yanza.kredit.model.DurationModel

class DurationAsync(var durationModel: MutableLiveData<DurationModel>, val errorString: MutableLiveData<String>) : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.YANZA_KREDIT + "duration"
            try {
                return HttpUtility.getRequestNoToken(url)
            } catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                val type = object : TypeToken<DurationModel>() {}.type
                val durModel = gson.fromJson<DurationModel>(result, type)
                try {
                    durationModel.value =  durModel
                }catch (e: Exception){
                    Log.e("INTEREESY",  e.message)
                    errorString.value = e.message
                }


            }
        }
    }
