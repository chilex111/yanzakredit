package com.yanza.kredit.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yanza.kredit.helper.showAlert
import com.yanza.kredit.viwModel.AuthenticationViewModel
import com.yanzu.kredit.R
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.dialog_reset_password.*
import kotlinx.android.synthetic.main.template_progress.*

class ForgotPasswordActivity : AppCompatActivity() {

    private var valueEntered : String ?= null
    private var dialog : Dialog?= null
    private var passText : String ?= null
    private var codeTExt : String ?= null
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)

        forgotPassword()

        buttonReset!!.setOnClickListener {
             codeTExt = editCode!!.text.toString()
             passText = editPassword!!.text.toString()
            val conPassText = editConfirmPassword!!.text.toString()

            if (codeTExt!!.isEmpty()) {
                editCode!!.error = "This field is required"

            }
            if (passText!!.isEmpty()){
                editPassword!!.error = "This field is required"
            }
            if(conPassText.isEmpty()){
                editConfirmPassword!!.error = "This field is required"
            }
            if (passText != conPassText){
                editConfirmPassword!!.error = "Password does not match"
            }
            else{
                relativeProgress.visibility = View.VISIBLE
                viewModel.reset(codeTExt!!, passText!!).observe(this, Observer {
                    relativeProgress.visibility = View.GONE
                    if (it.status!!){
                        showAlert(it.msg.toString())
                        startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                    }else{
                        showAlert(it.msg.toString())
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun forgotPassword() {
         dialog = Dialog(this)
        dialog!!.setContentView(R.layout.dialog_reset_password)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.onBackPressed()

        dialog!!.btnSubmit.setOnClickListener {
            dialog!!.relativeProgress.visibility = View.VISIBLE
             valueEntered = dialog!!.editEmail.text.toString()
            if (valueEntered.isNullOrEmpty()){
                dialog!!.editEmail.error = "This field is required"
            }else{
                viewModel.forgot(valueEntered!!).observe(this, Observer {
                    dialog!!.relativeProgress.visibility = View.GONE
                    if (it.status!!){
                        dialog!!.dismiss()
                        showAlert("Check your email account for your verification code to reset your password. Please check your spam box for your verification code")
                    }
                    else{
                       showAlert(it.data?.msg.toString())
                    }
                })

            }
        }
        dialog!!.show()
    }

}