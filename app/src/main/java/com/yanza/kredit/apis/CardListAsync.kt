package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.CardListModel

class CardListAsync(
    private var userId: Int,
    private var cardTokenModel: MutableLiveData<CardListModel>,
    var errorString: MutableLiveData<String>
): AsyncTask<Void, Int, String>(){

    override fun doInBackground(vararg p0: Void?): String? {

        val url = Const.YANZA_KREDIT + "usercards/" + userId
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
                val type= object : TypeToken<CardListModel>(){}.type
                val activeLoan = gson.fromJson<CardListModel>(result, type)
                    cardTokenModel.value = activeLoan


            } catch (e: Exception){
                if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.message.toString())
                errorString.value = e.message.toString()

            }
        }
        else{
            errorString.value = "return is empty"
        }
    }
}