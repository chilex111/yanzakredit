package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.*
import com.yanza.kredit.model.KeyModel

class KeyAsync(var keyModel: MutableLiveData<KeyModel>) : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.YANZA_KREDIT + "getkeys"
            try {
                return HttpUtility.getRequestNoToken(url)
            } catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                val type = object : TypeToken<KeyModel>() {}.type
                val model = gson.fromJson<KeyModel>(result, type)
                try {
                    keyModel.value = model
                }catch (e: Exception){
                    Log.e("INTEREESY",  e.message)
                }


            }
        }
    }
