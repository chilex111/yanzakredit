package com.yanza.kredit.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.yanzu.kredit.R
import com.yanza.kredit.helper.getStringPreference

class SplashActivity : AppCompatActivity() {
    private var splashLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= 21) {

            if (!splashLoaded) {

                setContentView(R.layout.activity_splash)
                val secondsDelay = 1
                Handler().postDelayed({
                    if (getStringPreference(R.string.login)=="1"){
                        if (getStringPreference(R.string.password).isNotEmpty()){
                            val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                            startActivity(intent)
                            this@SplashActivity.finish()
                        }
                        else{
                            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                            startActivity(intent)
                            this@SplashActivity.finish()
                        }

                    }else {
                        val intent = Intent(this@SplashActivity, AuthDashActivity::class.java)
                        startActivity(intent)
                        this@SplashActivity.finish()
                    }


                }, (secondsDelay * 3000).toLong())
                splashLoaded = true
            }
            /*else {

                val intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                startActivity(intent)
                this@SplashActivity.finish()

            }*/
        } else {
            Toast.makeText(this, "This App version is higher than you phone version. download the correct version for your phone", Toast.LENGTH_SHORT).show()
        }
    }
}
