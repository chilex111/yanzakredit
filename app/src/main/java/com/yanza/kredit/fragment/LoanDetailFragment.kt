package com.yanza.kredit.fragment


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yanza.kredit.activity.DashboardActivity
import com.yanzu.kredit.R
import com.yanza.kredit.enums.NavigationDirection
import com.yanza.kredit.helper.getIntPreference
import com.yanza.kredit.helper.getStringPreference
import com.yanza.kredit.helper.showAlert
import com.yanza.kredit.listener.FragmentListener
import com.yanza.kredit.model.loan_model.BankModel
import com.yanza.kredit.viwModel.MainViewModel
import fewchore.com.exolve.fewchore.model.CardModel
import fewchore.com.exolve.fewchore.model.FormModel
import kotlinx.android.synthetic.main.fragment_loan_detail.*
import kotlinx.android.synthetic.main.template_progress.*
import java.text.DecimalFormat
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
@SuppressLint("SetTextI18n")
class LoanDetailFragment : Fragment(), FragmentListener {
    private var loanModel:FormModel?= null
    private var cardInfo:CardModel?= null
    private var bankInfo: BankModel?= null
    private var listener: FragmentListener ?= null
    private var idselected: String ?= null
    private lateinit var viewModel: MainViewModel
    private var amountpercent:Double ? = null

    // private var loanForm:FormModel?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        txtLimit.text =
            if (activity!!.getStringPreference(R.string.loan_limit).isNotEmpty())
                "₦ ${activity!!.getStringPreference(R.string.loan_limit)}"
            else "₦ 0"; lin.visibility = View.GONE

        views()
        errorMsg()
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            var isManualChange = false

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (isManualChange) {
                    isManualChange = false
                    return
                }

                try {
                    val value = s.toString().replace(",", "")
                    val reverseValue = StringBuilder(value).reverse()
                        .toString()
                    val finalValue = StringBuilder()
                    for (i in 1..reverseValue.length) {
                        val `val` = reverseValue[i - 1]
                        finalValue.append(`val`)
                        if (i % 3 == 0 && i != reverseValue.length && i > 0) {
                            finalValue.append(",")
                        }
                    }
                    isManualChange = true
                    editAmount!!.setText(finalValue.reverse())
                    editAmount!!.setSelection(finalValue.length)
                } catch (e: Exception) {
                    // Do nothing since not a number
                }

            }
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }
        })
    }

    fun views(){
        editAmount!!.afterTextChanged {
            if (editAmount!!.text.toString().isNotEmpty()) {
                val textV = editAmount!!.text.toString().replace(",", "")
                val amount = Integer.valueOf(textV)
                val split = activity!!.getStringPreference(R.string.interest).split(":")
                val interest =split[0].toDouble()
                val deci = interest/ 100.0f
                 amountpercent = deci * amount
                val total = amountpercent!! + amount
                val decimalFormat = DecimalFormat("#,###.##")
                val am = decimalFormat.format(java.lang.Double.parseDouble(total.toString()))

                totalAmount!!.text = am.toString()
                val max = Integer.valueOf(txtLimit.text.toString().replace(",", "").replace(".00", "").replace("₦ ", ""))

                if (max > 0)
                    if (amount > max) {
                        val paramString = "Your loan plan is higher than your Maximum Loan Amount. Please request for a lower Amount "

                        val localBuilder = AlertDialog.Builder(activity!!)
                        localBuilder.setMessage(paramString)
                        localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                            clearAmt()

                        }
                        localBuilder.create().show()
                    }
            }

        }
        btnNext!!.setOnClickListener {
            submitForm()

        }
        viewModel.duration().observe(this, Observer {
            val durationList = ArrayList<String>()
            //val savedDuration = activity!!.getDurationToSharedPrefs(R.string.loan_duration)
            if (it.status){
                durationList.add("Loan Duration")
                for (i in it.data){
                    durationList.add("${i.value} days")
                }
            }

            val titleAdapter = ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item, durationList)
            titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDuration!!.adapter = titleAdapter


            spinnerDuration!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    var total: Double
                    if (i == 0) {
                        Log.i("PersonalFragment", "Nothing selected")
                    } else {
                        val v = adapterView.selectedItem.toString().replace(" days", "").toInt()
                        for (r in it.data){
                            if (v == r.value){
                                idselected =r.id.toString()
                                var flatRate = v/30
                                var totalpercent = if(flatRate <= 1){
                                    1 * amountpercent!!.toDouble()
                                } else{
                                    flatRate* amountpercent!!.toDouble()
                                }
                                val textV = editAmount!!.text.toString().replace(",", "").toDouble()

                                total = totalpercent + textV

                                val decimalFormat = DecimalFormat("#,###.##")
                                val am = decimalFormat.format(java.lang.Double.parseDouble(total.toString()))
                                println("AFter:: $am")
                                totalAmount!!.text = am.toString()
                            }
                        }
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                }
            }
        })

    }

    private fun clearAmt() {
        editAmount!!.text.clear()
        editAmount!!.requestFocus()

    }
    private fun errorMsg(){
        viewModel.error().observe(this, Observer {
            relativeProgress.visibility = View.GONE
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun submitForm(){

        val amount =editAmount!!.text.toString().replace(",","")
        if(amount.isNotEmpty()) {

            val split = activity!!.getStringPreference(R.string.interest).split(":")
            val interest =split[0].toDouble()
            val deci = interest/ 100.0f
            //  val percent = deci * Integer.valueOf(amount)
            val total = totalAmount!!.text.toString().replace(",", "")

            val max = Integer.valueOf(txtLimit.text.toString().replace(",", "").replace(".00", "").replace("₦ ", ""))

            if (idselected.isNullOrBlank()) {
                activity?.showAlert("Please select your Loan Duration")
                return
            }
            if (amount.isEmpty()) {
                editAmount!!.error = "This field i required"
                return
            }
            if (amount.toInt() <10000 ){
                activity!!.showAlert("Your loan amount is lower than your Minimum Loan Amount. Please request for an amount from 10,000 and above ")
                return
            }
            if (amount.toInt() >100000){
                activity!!.showAlert("Your loan amount is higher than your Maximum Loan Amount. Please request for an amount lower than 100,000")
                return
            }
            if (max > 0) {
                if (Integer.valueOf(amount) > max) {
                    activity!!.showAlert("Your loan plan is higher than your Maximum Loan Amount. Please request for a lower Amount ")
                    return
                }
            }
            else {
                loanModel = FormModel()

                loanModel!!.duration = idselected
                loanModel!!.loanAmount = amount
                loanModel!!.interest = interest.toString()
                loanModel!!.totalPayback = total
                loanModel!!.interest_id = split[1]
                childFragment(CardsFragment.newInstance(cardInfo, null, null))
            }
            // listener!!.onFormDetailSubmit(loanModel)
            //listener!!.onFragmentNavigation(NavigationDirection.FORM_FORWARD)
            // }
        }else {
            activity?.showAlert("Please enter a valid plan")
            return
        }
    }

    override fun onFragmentNavigation(navigationDirection: NavigationDirection) {
        when(navigationDirection){
            NavigationDirection.FORM_FORWARD ->{
                childFragment(CardsFragment.newInstance(cardInfo,null, null))
            }
            NavigationDirection.CARD_DETAILS_BACKWARD ->{
                container.visibility = View.GONE
            }
            NavigationDirection.CARD_DETAILS_FORWARD->{
                childFragment(BanksFragment.newInstance(bankInfo))
            }
            NavigationDirection.BANK_DETAILS_BACKWARD -> {
                childFragment(CardsFragment.newInstance(cardInfo, null, null))
            }
            NavigationDirection.BANK_DETAILS_FORWARD -> {
               // Toast.makeText(activity, "Toast", Toast.LENGTH_LONG).show()
                val msg1 = "Are you sure you want to request for a loan"
                val localBuilder = AlertDialog.Builder(this.activity!!)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    localBuilder.setMessage(Html.fromHtml(msg1, Html.FROM_HTML_MODE_LEGACY))
                }
                else{
                    localBuilder.setMessage(Html.fromHtml(msg1))
                }
                localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                    relativeProgress.visibility = View.VISIBLE
                    viewModel.requestLoan(loanModel, cardInfo,bankInfo, activity!!.getIntPreference(R.string.user_id)).observe(this, Observer {response ->
                        relativeProgress.visibility = View.GONE

                        if (response.status!!){
                            activity!!.showAlert(response.msg!!)
                            activity!!.showAlert("Your request has been sent and is been reviewed for approval.")
                            (activity as DashboardActivity).firstTimer()
                        }else{
                            activity!!.showAlert(response.msg!!)
                        }
                    })
                    // Pay100Async().execute()
                    // LoanRequestAsync().execute()
                }
                localBuilder.setNegativeButton(R.string.cancel){_,_->
                    localBuilder.create().dismiss()
                    clearAmt()
                }
                localBuilder.create().show()
            }
        }
    }

    override fun onFormDetailSubmit(formModel: FormModel) {
    }
    override fun onCardDetailSubmit(cardModel: CardModel) {
        cardInfo = cardModel
    }

    override fun onBankDetailSubmit(bankModel: BankModel) {
        bankInfo = bankModel
    }
    private fun childFragment(fragmentClass: Fragment){
        childFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragmentClass)
            .addToBackStack(null)
            .commit()
    }




}
