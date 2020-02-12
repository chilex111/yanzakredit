package com.yanza.kredit.apis

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.AccessCodeModel
import org.json.JSONObject
import java.lang.IllegalStateException

@SuppressLint("StaticFieldLeak")
class AddCardAsync(
    private val id: Int,
    private val accessCode: MutableLiveData<AccessCodeModel>,
    var errorString: MutableLiveData<String>
) : AsyncTask<Void, Int, String>() {

    override fun doInBackground(vararg p0: Void?): String? {
        val url = Const.YANZA_KREDIT+"addcard"

        val map = JSONObject()

        map.put("user_id",id)
        try {
            return HttpUtility.postJsonAuth(url,map)
        }catch (e:Exception){}
        return null

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null){
            val gson = Gson()
            try{
                val codeModel = gson.fromJson<AccessCodeModel>(result, AccessCodeModel::class.java)
                accessCode.value = codeModel
            } catch (e: Exception){
                if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.message.toString())
                errorString.value = e.message
            }
        }
        else   errorString.value = "return is empty"
    }
}