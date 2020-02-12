package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.LoginModel
import org.json.JSONObject

class LoginAsync(
    private val emailText: String,
    private val passwordText: String,
    private val loginModel: MutableLiveData<LoginModel>,
    private var errorString: MutableLiveData<String>
) :
    AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.YANZA_KREDIT+"login"

            try {
                val map = JSONObject()
                map.put("email", emailText)
                map.put("password", passwordText)
                return HttpUtility.postJson(url, map)
            }catch (e:Exception){
                Log.i("TAG_LOGIN", e.message)
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                    val gson = Gson()
                    try{
                        val type = object : TypeToken<LoginModel>() {}.type
                        val userModel = gson.fromJson<LoginModel>(result, type)
                       // if (userModel != null) {
                            loginModel.value = userModel
                       // }
                    }catch (e: Exception){
                        //    Toast.makeText(this@LoginActivity, "Please check your network connection", Toast.LENGTH_LONG).show()
                        Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                        errorString.value = result
                    }
                }else
                    errorString.value = "Invalid email or password"
            }

    }
