package com.yanza.kredit.apis

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.BankListModel
import java.lang.IllegalStateException

@SuppressLint("StaticFieldLeak")
class BankListAsync(
    private val bankListModel: MutableLiveData<BankListModel>,
    val errorString: MutableLiveData<String>
) : AsyncTask<Void, Int, String>() {

    override fun doInBackground(vararg p0: Void?): String? {
        val url = Const.YANZA_KREDIT+"bank"
        try {
            return HttpUtility.getRequest(url)
        }catch (e: Exception){}
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null){
            try {
                val gson = Gson()
                val bankList = gson.fromJson<BankListModel>( result, BankListModel::class.java)
                bankListModel.value = bankList
            }catch (e: Exception){
                if (e is IllegalStateException)
                  errorString.value = e.message
            }


        }
    }
}