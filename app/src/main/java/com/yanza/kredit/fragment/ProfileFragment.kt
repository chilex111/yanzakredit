package com.yanza.kredit.fragment


import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yanza.kredit.helper.getGenderToSharedPrefs
import com.yanzu.kredit.R
import com.yanza.kredit.helper.getStringPreference
import com.yanza.kredit.model.Register
import com.yanza.kredit.viwModel.MainViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import kotlinx.android.synthetic.main.dialog_edit_profile.photoButton
import kotlinx.android.synthetic.main.dialog_edit_profile.spinnerGender
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.editDOB
import kotlinx.android.synthetic.main.fragment_profile.editEmail
import kotlinx.android.synthetic.main.fragment_profile.editName
import kotlinx.android.synthetic.main.fragment_profile.editPhone
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    var dialog :Dialog?= null
    private var TAKE_PHOTO_REQUEST = 14
    private var photo1: String? = null
    private var gender: String? = null
    private var genderIdSelected: Int? = null
    private var genderSelected: String? = null
    private var aDay: Int = 0
    private var aMonth: Int = 0
    private var aYear: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(activity!!)
            .load(activity!!.getStringPreference(R.string.profile))
            .into(imgProfile)

        val g = activity?.getGenderToSharedPrefs(R.string.array_gender)
        if (g != null) {
           /* for (i in g) {
                if (activity!!.getStringPreference(R.string.gender) == i.name)
                    genderIdSelected = i.id

            }*/
        }
        val name = activity!!.getStringPreference(R.string.first_name)+" "+activity!!.getStringPreference(R.string.surname)
        editName.text = name
        editEmail.text = activity!!.getStringPreference(R.string.email)
        editPhone.text = activity!!.getStringPreference(R.string.phone_number)
        editSex.text = activity!!.getStringPreference(R.string.gender)
        editDOB.text = activity!!.getStringPreference(R.string.date_of_birth)
        editAddress.text = activity!!.getStringPreference(R.string.office_address)

        txtEdit.setOnClickListener {
            editProfile()
        }
    }

    private fun editProfile(){
         dialog = Dialog(activity!!, R.style.Dialog)
        dialog!!.setContentView(R.layout.dialog_edit_profile)
        dialog!!.show()

        dialog!!.setCancelable(false)
        dialog!!.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.editDOB.setOnClickListener {
            val calendar = Calendar.getInstance()
            aDay = calendar.get(Calendar.DAY_OF_MONTH)
            aMonth = calendar.get(Calendar.MONTH)
            aYear = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(activity!!,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    // buttonCalendar.text = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                    val startDates = String.format("%02d",dayOfMonth)+  "-" + String.format("%02d",month + 1) + "-"  +year.toString()
                    editDOB.text =startDates
                }, aYear, aMonth, aDay

            )

            //  DateText = txtDate.getText().toString().trim();
            datePickerDialog.show()

        }
        val g = activity?.getGenderToSharedPrefs(R.string.array_gender)
        val acctTypeList = ArrayList<String>()
        acctTypeList.add("Gender")
        if (g != null) {
            for (i in g){
                acctTypeList.add(i.name.toString())
            }
        }
        val genderAdapter = ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item, acctTypeList)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialog!!.spinnerGender.adapter = genderAdapter
       val gender =  activity!!.getStringPreference(R.string.gender)
        if (gender.isNotEmpty()) {
            val selectedPosition = acctTypeList.indexOf(gender)
            dialog!!.spinnerGender.setSelection(selectedPosition)

        }
        dialog!!.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        dialog!!.btnEdit.setOnClickListener {

            updateProfile( Register(
                firstName = activity!!.getStringPreference(R.string.first_name),
                surname = activity!!.getStringPreference(R.string.surname),
                email =  dialog!!.editEmail.text.toString(),
                address = dialog!!.editAddress1.text.toString(),
                phone =  dialog!!.editPhone.text.toString(),
                gender = genderIdSelected!!,
                office_address =  dialog!!.editAddress.text.toString(),
                photo =  photo1?.replace("\n","").toString()
            ,password = "",
                dob = dialog!!.editDOB.text.toString())
            )
        }
        Glide.with(activity!!)
            .load(activity!!.getStringPreference(R.string.profile))
            .into(dialog!!.imgPhoto1)

        val name = activity!!.getStringPreference(R.string.first_name)+" "+activity!!.getStringPreference(R.string.surname)
        dialog!!.editName.text = name
        dialog!!.editEmail.text = activity!!.getStringPreference(R.string.email)
        dialog!!.editPhone.text = activity!!.getStringPreference(R.string.phone_number)
        dialog!!.editPhone.text = activity!!.getStringPreference(R.string.phone_number)

       genderSelected = (activity!!.getStringPreference(R.string.gender))
        dialog!!.editDOB.text = activity!!.getStringPreference(R.string.date_of_birth)
        dialog!!.editAddress1.setText(activity!!.getStringPreference(R.string.office_address))
        dialog!!.photoButton.setOnClickListener {
           cameraClicked()
       }

    }

    private fun updateProfile(register: Register) {
        viewModel.updateProfile(register)
    }

    private fun cameraClicked(){
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
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
                    ActivityCompat.requestPermissions(activity!!,
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

            dialog?.photoButton!!.setImageBitmap(profileImg)

            val bao = ByteArrayOutputStream()
            profileImg.compress(Bitmap.CompressFormat.PNG, 90, bao)
            val ba = bao.toByteArray()

            photo1 = Base64.encodeToString(ba, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}
