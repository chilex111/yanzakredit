package com.yanza.kredit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yanza.kredit.apis.GenderAsync
import com.yanzu.kredit.R
import kotlinx.android.synthetic.main.activity_auth_dash.*

class AuthDashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_dash)
        GenderAsync().execute()

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
    }
}
