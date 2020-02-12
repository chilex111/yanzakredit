package com.yanza.kredit.apis

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.Const
import com.yanza.kredit.helper.HttpUtility
import com.yanza.kredit.model.Register
import com.yanza.kredit.model.SignupModel
import org.json.JSONObject
import java.io.IOException

class SignUpAsync(private val register: Register, var responseModel: MutableLiveData<SignupModel>, var errorString: MutableLiveData<String>)
    : AsyncTask<Void, Int, String>() {

        override fun doInBackground(vararg voids: Void): String? {
            return try {
                val map = JSONObject()
                map.put("firstname", register.firstName)
                map.put("lastname",register.surname)
                map.put("email", register.email)
                map.put("phone", register.phone)
                map.put("password", register.password)
                map.put("image", register.photo)
                map.put("address", register.address)
                map.put("office_address", register.office_address)
                map.put("dob", register.dob)
                map.put("gender_id", register.gender)

                HttpUtility.postJson(Const.YANZA_KREDIT + "register", map)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }

        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (!s.isNullOrEmpty()) {
                try {
                    val responseType = object : TypeToken<SignupModel>() {}.type
                    val response = Gson().fromJson<SignupModel>(s, responseType)
                    responseModel.value = response

                } catch (e: Exception) {
                    if (e is IllegalStateException) {
                        Log.i("LOGIN", "Please check your network connection")
                    }
                    errorString.value = e.message

                }

            }else   errorString.value = "return is empty"
        }
    }