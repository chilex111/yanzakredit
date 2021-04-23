package com.yanza.kredit.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yanza.kredit.apis.GenderAsync
import com.yanzu.kredit.R
import com.yanza.kredit.helper.saveToSharedPreference
import com.yanza.kredit.helper.showAlert
import com.yanza.kredit.helper.showSnack
import com.yanza.kredit.viwModel.AuthenticationViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.template_progress.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)

        GenderAsync().execute()
        error()
        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnLogin.setOnClickListener {
            loginClicked()
        }
        btnRegister.setOnClickListener {
            registerClicked()
        }
    }

    private fun error(){
        viewModel.error().observe(this, Observer {
            relativeProgress.visibility = View.GONE
            if (!it.isNullOrEmpty()){
                if (it.contains("Unauthorised", true))showAlert("Be sure your Email and password is correct")
                else showSnack("$it")
            }

            //
        })
    }
    fun loginClicked(){
        //fixme api call here

        if(editEmail.text.toString().isNotEmpty() && (editPassword.text.toString().isNotEmpty())) {
            relativeProgress.visibility = View.VISIBLE
            viewModel.login(editEmail.text.toString(), editPassword.text.toString())
                .observe(this, Observer { userModel ->
                    relativeProgress.visibility = View.GONE

                    if (userModel.status!!) {
                        val loginModel = userModel.data

                        saveToSharedPreference(R.string.email, editEmail.text.toString())
                        saveToSharedPreference(R.string.token, userModel.token!!)
                        saveToSharedPreference(R.string.user_id, userModel.data?.userId!!)
                        saveToSharedPreference(R.string.login, "1")
                        saveToSharedPreference(
                            R.string.first_name,
                            loginModel?.firstname.toString()
                        )
                        saveToSharedPreference(R.string.email, loginModel?.email.toString())

                        saveToSharedPreference(R.string.gender, loginModel?.gender.toString())
                        saveToSharedPreference(R.string.date_of_birth, loginModel?.dob.toString())
                        saveToSharedPreference(
                            R.string.office_address,
                            loginModel?.officeAddress.toString()
                        )
                        saveToSharedPreference(
                            R.string.home_address,
                            loginModel?.address.toString()
                        )

                        saveToSharedPreference(R.string.phone_number, loginModel?.phone.toString())
                        saveToSharedPreference(R.string.surname, loginModel?.lastname.toString())
                        saveToSharedPreference(R.string.profile, loginModel?.image.toString())

                        //saveToSharedPreference(R.string.status, loginModel?.userLoanstatus.toString())

                        startActivity(Intent(this, LogsActivity::class.java))

                    }
                })
        }
        else
            showAlert("Enter your login details to proceed")
    }
    fun registerClicked(){
        startActivity(Intent(this, RegisterActivity::class.java))

    }
    companion object {
        fun start(activity: Activity){
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
