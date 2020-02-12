package com.yanza.kredit.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yanza.kredit.apis.GenderAsync
import com.yanzu.kredit.R
import com.yanza.kredit.helper.*
import com.yanza.kredit.model.Register
import com.yanza.kredit.listener.PinTextListener
import com.yanza.kredit.viwModel.AuthenticationViewModel
import com.yanza.kredit.viwModel.MainViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_bvn.editBVN
import kotlinx.android.synthetic.main.dialog_bvn.progressBVN
import kotlinx.android.synthetic.main.dialog_verify.*
import kotlinx.android.synthetic.main.fragment_bank.*
import kotlinx.android.synthetic.main.layout_success.*
import kotlinx.android.synthetic.main.t_c.*
import kotlinx.android.synthetic.main.template_progress.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("SetTextI18n")
class RegisterActivity : AppCompatActivity(), PinTextListener {

    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var viewModel1: MainViewModel
    private var acctTypeSelected: String?= null
    private var genderSelected: String?= null
    private var genderIdSelected: Int=-1
    private var bankIdSelected: String?= null
    private var bankSelected: String?= null
    private var acctTypeId: Int? = null
    var dialog :Dialog?= null
    private var TAKE_PHOTO_REQUEST = 14
    private var photo1: String? = null
    private var enteredBVN : String ?= null
    private var userId : Int ?= null
    private var aDay: Int = 0
    private var aMonth: Int = 0
    private var aYear: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel1 = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
        GenderAsync().execute()
        views()
    }

    fun views(){

        btnRegister.setOnClickListener {
            registerClicked()
        }
        btnLogin.setOnClickListener {
            loginClicked()
        }
        terms.setOnClickListener {
            t_c()
        }
        editDOB.setOnClickListener {
            val calendar = Calendar.getInstance()
            aDay = calendar.get(Calendar.DAY_OF_MONTH)
            aMonth = calendar.get(Calendar.MONTH)
            aYear = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    // buttonCalendar.text = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                    val startDates = String.format("%02d",dayOfMonth)+  "-" + String.format("%02d",month + 1) + "-"  +year.toString()
                    editDOB.text =startDates
                }, aYear, aMonth, aDay

            )

            //  DateText = txtDate.getText().toString().trim();
            datePickerDialog.show()

        }
        val g = getGenderToSharedPrefs(R.string.array_gender)
        val acctTypeList = ArrayList<String>()
        acctTypeList.add("Gender")
        if (g != null) {
            for (i in g){
                acctTypeList.add(i.name.toString())
            }
        }
        val genderAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, acctTypeList)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (i == 0) {
                    Log.i("PersonalFragment", "Nothing selected")
                } else {
                    genderSelected = adapterView.selectedItem.toString()
                    if (g != null)
                        for (i in g) {
                            if (genderSelected == i.name)
                                genderIdSelected = i.id

                        }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }

        photoButton.setOnClickListener {
            cameraClicked()
        }
        viewModel.error().observe(this, Observer {
            relativeProgress.visibility = View.GONE
            showSnack("$it")
        })
    }

    fun loginClicked(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
private fun t_c(){
    val d = Dialog(this, R.style.Dialog)
    d.setContentView(R.layout.t_c)
    d.btnClose1.setOnClickListener{
        d.dismiss()
    }
    d.txtT_C.movementMethod = ScrollingMovementMethod()
    d.show()
}
    fun registerClicked(){
        //fixme call api
        if (validate()) {
            relativeProgress.visibility = View.VISIBLE
            viewModel.register(
                Register(
                    firstName = editFirstName.text.toString(),
                    surname = editSurname.text.toString(),
                    email = editEmail.text.toString(),
                    address = editHomeAddress.text.toString(),
                    password = editPassword.text.toString(),
                    phone =  editPhone.text.toString(),
                    gender = genderIdSelected,
                    dob = editDOB.text.toString(),
                    photo =  photo1.toString(),
                    office_address = editOfficeAddress.text.toString()
                )
            ).observe(this, Observer {
                relativeProgress.visibility = View.GONE
                if (it.status!!) {

                    userId = it.data?.id
                    saveToSharedPreference(R.string.token, it.token!!)
                    saveToSharedPreference(R.string.user_id, it.data?.id!!)
                    //verifyBVN(it.data.id)
                    verifyPhone()

                } else {
                    showAlertError("Registration was Not Successful ", it.msg.toString())
                }
            })
        }
        //startActivity(Intent(this, RegisterActivity::class.java))

    }

    fun addBank() {
//        textEmpty!!.visibility = View.GONE
        val dialogBank = Dialog(this, R.style.Dialog)
        dialogBank.setContentView(R.layout.fragment_bank)
        dialogBank.setCanceledOnTouchOutside(false)
        val next = dialogBank.findViewById(R.id.buttonNext)as Button
        dialogBank.btnClose.setOnClickListener {
            dialogBank.dismiss()
        }
        dialogBank.inputName.visibility = View.GONE
        dialogBank.progressBVN.visibility = View.GONE

        dialogBank.editAccountName.setTextIsSelectable(false)
        dialogBank.editBVN.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length ==11){
                    val enteredBVN = p0.toString()
                    /*
                    internetCheck = 2
                    InternetCheckAsync().execute()*/

                    //  if (hasActiveInternetConnection(this@RegisterActivity)){
                    dialogBank.progressBVN.visibility = View.VISIBLE
                    verifyBVN(enteredBVN,  dialogBank.progressBVN,dialogBank.editBVN,dialogBank.editAccountName.text.toString())
                    /* viewModel.verifyBVN(enteredBVN, getIntPreference(R.string.user_id)).observe(this@RegisterActivity, Observer {
                         dialogBank.progressBVN.visibility = View.GONE
                     })*/
                    /* }else{
                        showAlert("Please check your Internet connection")
                     }*/

                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        dialogBank.editAccountNo.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length ==10) {
                    dialogBank.progressName.visibility = View.VISIBLE
                    viewModel1.accountName(p0.toString(), bankSelected).observe(this@RegisterActivity, Observer {
                        dialogBank.inputName.visibility = View.VISIBLE
                        dialogBank.progressName.visibility = View.GONE
                        if (it.status!!){
                            dialogBank.editAccountName.setText(it.data?.name)

                        }else{
                            dialogBank.editAccountName.setText(it.data?.name)
                        }
                    })
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


        })

        viewModel1.banks().observe(this, Observer {
            val bankList  = it.data
            val lists = ArrayList<String>()
            lists.add("Select Bank")
            if (bankList != null) {
                for (s in bankList) {
                    lists.add(s?.name.toString())
                }
            }
            val titleAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, lists)
            titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            dialogBank.spinnerBankName.adapter = titleAdapter
            /* if (bankSelected != null) {
                 val selectedPosition = lists.indexOf(bankSelected!!)
                 dialogBank.spinnerBankName.setSelection(selectedPosition)

             }
 */
            dialogBank.spinnerBankName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    if (i == 0) {
                        Log.i("PersonalFragment", "Nothing selected")
                    } else {
                        bankSelected = adapterView.selectedItem.toString()
                        if (bankList != null) {
                            for (b in bankList){
                                if (bankSelected == b?.name)
                                    bankIdSelected = b?.id.toString()
                            }

                        }
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                }
            }
        })



        val acctTypeList = ArrayList<String>()
        acctTypeList.add("Account Type")
        acctTypeList.add("Credit")
        acctTypeList.add("Saving")
        acctTypeList.add("Current")
        val acctAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, acctTypeList)
        acctAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBank.spinnerAcctType.adapter = acctAdapter

        if (acctTypeSelected != null) {
            val selectedPosition = acctTypeList.indexOf(acctTypeSelected!!)
            dialogBank.spinnerAcctType.setSelection(selectedPosition)

        }
        dialogBank.spinnerAcctType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (i == 0) {
                    Log.i("PersonalFragment", "Nothing selected")
                } else {
                    acctTypeSelected = adapterView.selectedItem.toString()
                }
                acctTypeId= i
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }

        dialogBank.buttonNext.setOnClickListener {
            dialogBank.relativeProgress.visibility = View.VISIBLE
            if (getBooleanPreference(R.string.bvn))
                viewModel1.addAcct(dialogBank.editAccountNo.text.toString(), bankIdSelected!!,
                    getIntPreference(R.string.user_id), acctTypeSelected!!).observe(this, Observer {
                    dialogBank.relativeProgress.visibility = View.GONE
                    if (it.status!!){
                        successDialog()

                    }
                })
            else
                showAlert("Enter a valid Bank Verification Number(BVN)")

        }
        dialogBank.show()
    }

    private fun successDialog() {
        val dialog = Dialog(this, R.style.Dialog)
        dialog.setContentView(R.layout.layout_success)
        dialog.setCanceledOnTouchOutside(false)
        dialog.txt.text = "Your Registration was Successful"
        dialog.btnNext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        dialog.show()

    }

    private fun verifyBVN(
        enteredBVN: String,
        progressBVN: ProgressBar,
        editBVN: EditText,
        acctName: String
    ) {
        viewModel.verifyBVN(enteredBVN, userId!!).observe(this@RegisterActivity, Observer {
            progressBVN.visibility = View.GONE
            if (it.status!!){
                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                if (acctName.contains(it.data?.firstName.toString(), true) || acctName.contains(it.data?.lastName.toString(), true)) {
                    editBVN.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0)
                    saveToSharedPreference(R.string.bvn, true)
                }
                else{
                    showAlert("The BVN you enter does not match the account you registered with. Please enter your registered BVN")
                    editBVN.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.ic_error, 0)
                }

            }
            else{
                //acctID.setCompoundDrawables(null,null,activity!!.resourFces.getDrawable(R.drawable.ic_close),null)

                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                editBVN.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.ic_close, 0)
                // }
                editBVN.error =  "Your BVN could not be verified. Please try again"
            }
        })
    }

    private fun verifyPhone() {
        dialog = Dialog(this, R.style.Dialog)
        dialog!!.setContentView(R.layout.dialog_verify)
        dialog!!.pinChar1!!.addTextChangedListener(PinFormatter( dialog!!.pinChar2, this))
        dialog!!.pinChar2!!.addTextChangedListener(PinFormatter( dialog!!.pinChar3, this))
        dialog!!.pinChar3!!.addTextChangedListener(PinFormatter( dialog!!.pinChar4, this))
        dialog!!.pinChar4!!.addTextChangedListener(PinFormatter( dialog!!.pinChar5, this))
        dialog!!.pinChar5!!.addTextChangedListener(PinFormatter( dialog!!.pinChar6, this))
        dialog!!.pinChar6!!.addTextChangedListener(PinFormatter( dialog!!.pinChar1, this))
        dialog!!.show()
    }

    override fun onTextChanged() {

        val str1 =  dialog!!.pinChar1!!.text.toString()
        val str2 =  dialog!!.pinChar2!!.text.toString()
        val str3 =  dialog!!.pinChar3!!.text.toString()
        val str4 =  dialog!!.pinChar4!!.text.toString()
        val str5 =  dialog!!.pinChar5!!.text.toString()
        val str6 =  dialog!!.pinChar6!!.text.toString()

        if (str1.isNotEmpty() && str2.isNotEmpty() && str3.isNotEmpty() && str4.isNotEmpty() && str5.isNotEmpty() && str6.isNotEmpty()) {
            val code = str1 + str2 + str3 + str4 + str5 + str6

            if (code.isEmpty()) {
                pinChar1!!.error = "This field is required"
            } else {
                dialog!!.relativeProgress!!.visibility = View.VISIBLE
                viewModel.confirmOTP(code, userId!!).observe(this, Observer {response->
                    if (response.status!!) {
                        addBank()
                    }else{
                        showAlert(response.msg.toString())
                    }

                })
            }
        }
    }

    private fun validate():Boolean{

        /*   if (officeAddressText!!.isEmpty()) {
               editOfficeAddress!!.error = "This field is required"
               return false
           }
           if (officeText!!.isEmpty()) {
               editPlaceOfWork!!.error = "This field is required"
               return false
           }*/
        if (!checkbox!!.isChecked){
            showAlert("Please check the terms and condition to proceed")
            return false
        }
        if (genderIdSelected == -1) {
            showAlert("Please select your gender")
            return false
        }
        if (editDOB.text.toString().isEmpty()) {
            editDOB.error = "Please select your Date of Birth"

            return false
        }

        if (editEmail.text.toString().isEmpty()) {
            editEmail!!.error = "This field is required"
            return false
        }
        if (editHomeAddress.text.toString().isEmpty()) {
            editHomeAddress!!.error = "This field is required"
            return false
        }
        if (editOfficeAddress.text.toString().isEmpty()) {
            editOfficeAddress!!.error = "This field is required"
            return false
        }
        if (editSurname.text.toString().isEmpty()) {
            editSurname!!.error = "This field is required"
            return false
        }
        if (editPassword.text.toString().isEmpty()) {
            editPassword!!.error = "This field is required"
            return false
        }
        if (editPassword.text.toString() != editConPassword.text.toString()) {
            inputConPasswrd!!.error = "Password does not match"
            return false
        }
        if (editPhone.text.toString().isEmpty() || editPhone.text.toString().length != 11) {
            editPhone!!.error = "This field is required"
            return false
        }
        if (editFirstName.text.toString().isEmpty()) {
            editFirstName!!.error = "This field is required"
            return false
        }
        if (!editEmail.text.toString().isEmailValid()){
            editEmail!!.error = "This is an Invalid Email"
            return false
        }
        return if (photo1.isNullOrEmpty()){
            showAlert("Please take a photo for your profile")
            false
        }
        else{
            true
        }
    }

    private fun cameraClicked(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                TAKE_PHOTO_REQUEST)
        }else{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            TAKE_PHOTO_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)

                } else {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA),
                        TAKE_PHOTO_REQUEST) }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            val profileImg = data?.extras?.get("data") as Bitmap

            imgPhoto!!.setImageBitmap(profileImg)

            val bao = ByteArrayOutputStream()
            profileImg.compress(Bitmap.CompressFormat.PNG, 90, bao)
            val ba = bao.toByteArray()

            photo1 = Base64.encodeToString(ba, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}
