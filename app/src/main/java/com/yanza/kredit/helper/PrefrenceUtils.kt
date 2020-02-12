package com.yanza.kredit.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.ConnectivityManager
import android.os.NetworkOnMainThreadException
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yanzu.kredit.R
import com.yanza.kredit.activity.LoginActivity
import com.yanza.kredit.model.GenderData
import com.yanza.kredit.model.GenderModel
import com.yanza.kredit.model.LoanTimModel
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


private  var PREFS_NAME = "YANZA_KREDIT"

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Activity.showSnack(text: String) = Snackbar.make(this.findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show()

fun Context.showAlert(paramString: String) {
    val localBuilder = AlertDialog.Builder(this)
    localBuilder.setMessage(paramString)
    localBuilder.setNeutralButton(R.string.ok) { _, _ -> }
    localBuilder.create().show()
}
fun Context.showAlertError(title:String, paramString: String) {
    val localBuilder = AlertDialog.Builder(this)
    localBuilder.setTitle(title)
    localBuilder.setMessage(paramString)
    localBuilder.setNeutralButton(R.string.ok) { _, _ -> }
    localBuilder.create().show()
}


fun setTextViewDrawableColor(textView: TextView, color: Int) {
    textView.setTextColor(color)
    for (drawable in textView.compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(textView.context, color), PorterDuff.Mode.SRC_IN)
        }
    }

}

//add data
fun Context.saveValueToSharedPrefs(@StringRes res: Int, model: List<Any>) {
    val editor = getPrefs(this).edit()
    val key = getString(res)
    val gson = Gson()
    val jsonCart = gson.toJson(model)
    editor.putString(key, jsonCart)
    editor.apply()
}

fun Context.getDurationToSharedPrefs(@StringRes key: Int): ArrayList<LoanTimModel>? {
    val prefs = getPrefs(this)
    val cartData: List<LoanTimModel>
    if (prefs.contains(getString(key))) {
        val jsonCart = prefs.getString(getString(key), null)
        val gson = Gson()
        val collectionType = object : TypeToken<List<LoanTimModel>>() {}.type
        val cartItems = gson.fromJson<List<LoanTimModel>>(jsonCart, collectionType)
        // val cartItems = gson.fromJson<EduPlanTypeModel>(jsonCart, EduPlanTypeModel::class.java)

        //  cartData = Arrays.asList(cartItems)
        cartData = ArrayList<LoanTimModel>(cartItems)
    } else {
        return try {
            ArrayList()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
    return cartData
}

fun Context.getGenderToSharedPrefs(@StringRes key: Int): ArrayList<GenderData>? {
    val prefs = getPrefs(this)
    val cartData: List<GenderData>
    if (prefs.contains(getString(key))) {
        val jsonCart = prefs.getString(getString(key), null)
        val gson = Gson()
        val collectionType = object : TypeToken<List<GenderData>>() {}.type
        val cartItems = gson.fromJson<List<GenderData>>(jsonCart, collectionType)
        // val cartItems = gson.fromJson<EduPlanTypeModel>(jsonCart, EduPlanTypeModel::class.java)

        //  cartData = Arrays.asList(cartItems)
        cartData = ArrayList<GenderData>(cartItems)
    } else {
        return try {
            ArrayList()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
    return cartData
}

fun Context.getIntPreference(@StringRes key: Int): Int {
    val prefs = getPrefs(this)
    return prefs.getInt(getString(key), -1)
}


fun Context.getStringPreference(@StringRes key: Int): String {
    val value = getString(key)
    val prefs = getPrefs(this)
    return prefs.getString(value, "")
}

fun Context.getBooleanPreference(@StringRes key: Int, defaultValue: Boolean = false): Boolean {
    val prefs = getPrefs(this)
    return prefs.getBoolean(getString(key), defaultValue)
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null
}
fun Context.hasActiveInternetConnection(context: Context): Boolean {
    if (isNetworkAvailable()) {
        try {
            val urlc = URL("http://www.google.com").openConnection() as HttpURLConnection
            urlc.setRequestProperty("User-Agent", "Test")
            urlc.setRequestProperty("Connection", "close")
            urlc.connectTimeout = 1500
            urlc.connect()
            return urlc.responseCode == 200
        } catch (e: Exception) {
            if (e is IOException)
                Log.e("NETWORk", "Error checking internet connection", e)
            if (e is NetworkOnMainThreadException)
                Log.e("NETWORk", " connection", e)

        }

    } else {
        Log.d("NETWORk", "No network available!")
    }
    return false
}


private fun getPrefs(context: Context): SharedPreferences {

    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}


fun clearSharedPreference(context: Context) {
    val settings: SharedPreferences
    val editor: SharedPreferences.Editor

    //settings = PreferenceManager.getDefaultSharedPreferences(contest);
    settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    editor = settings.edit()

    editor.clear()
    editor.apply()
}


fun Context.logout(activity: Context){

    activity.let {
        it.clearPrefsProperty(getString(R.string.token))
        it.clearPrefsProperty(getString(R.string.user_id))
        it.clearPrefsProperty(getString(R.string.login))
    }
    LoginActivity.start(activity as Activity)
    activity.finish()
}
fun Context.clearPrefsProperty(key: String) {
    getPrefs(this).edit().remove(key).apply()
}
fun Context.saveToSharedPreference(@StringRes res: Int, value: Any) {
    val editor = getPrefs(this).edit()
    val key = getString(res)
    when (value) {
        is String -> editor.putString(key, value).apply()
        is Int -> editor.putInt(key, value).apply()
        is Boolean -> editor.putBoolean(key, value).apply()
        is Long -> editor.putLong(key, value).apply()
        is Double -> editor.putLong(key, java.lang.Double.doubleToRawLongBits(value)).apply()
        else -> throw IllegalArgumentException("Not supported value set into preferences")
    }
}
