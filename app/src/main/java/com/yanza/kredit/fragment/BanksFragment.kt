package com.yanza.kredit.fragment


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanza.kredit.adapter.BankAdapter
import com.yanza.kredit.enums.NavigationDirection
import com.yanza.kredit.helper.getBooleanPreference
import com.yanza.kredit.helper.getIntPreference
import com.yanza.kredit.helper.showAlert
import com.yanza.kredit.listener.AdapterListener
import com.yanza.kredit.listener.CardListListener
import com.yanza.kredit.listener.FragmentListener
import com.yanza.kredit.listener.StringListener
import com.yanza.kredit.model.CardDetails
import com.yanza.kredit.model.loan_model.BankModel
import com.yanza.kredit.viwModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_bank.*
import kotlinx.android.synthetic.main.fragment_cards.*
import kotlinx.android.synthetic.main.template_progress.*
import com.yanzu.kredit.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BanksFragment : Fragment(), CardListListener, StringListener {
    override fun cardDetailsListener(cardDetails: MutableList<CardDetails>?, msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // TODO: Rename and change types of parameters
    private var bankModel: BankModel? = null
    private var param2: String? = null
    private lateinit var viewModel: MainViewModel
    private var pageValue: String? = null
    private var acctTypeSelected: String?= null
    private var bankSelected: String?= null
    private var acctTypeId: Int? = null
    private var listener: FragmentListener? = null
    private var bankIdSelected: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BankAdapter.stringlistener = this
        arguments?.let {
            bankModel = it.getParcelable(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
       /* if (pageValue!= null){
            if(pageValue.equals("1") ||pageValue.equals("2")){
                cardContainer!!.setBackgroundColor(activity!!.resources.getColor(R.color.green))
                buttonFrame!!.visibility = View.GONE
            }
        }*/
        txt.text = getString(R.string.my_bank)

        bankList()

        addCard.setOnClickListener {
            addBank(null, null, null, null)

        }
    }

    private fun bankList(){
        relativeProgress.visibility = View.VISIBLE
        viewModel.bankList(activity!!.getIntPreference(R.string.user_id)).observe(this, Observer {
            relativeProgress.visibility = View.GONE
            if (it.status!!){
                if (!it.data.isNullOrEmpty()) {
                    val bankAdapter = BankAdapter(it.data, activity!!, pageValue)
                    recyclerView!!.layoutManager = LinearLayoutManager(activity)
                    recyclerView.adapter = bankAdapter
                    recyclerView.setHasFixedSize(true)
                    recyclerView.requestFocus()
                }
                else{
                    textEmpty!!.visibility = View.VISIBLE
                }
            }
            else{
                textEmpty!!.visibility = View.VISIBLE
            }
        })
    }
    fun addBank(s: String?, bankName: String?, acctType: String?, acctNumber: String?) {
        textEmpty!!.visibility = View.GONE
       val dialogBank = Dialog(activity!!, R.style.Dialog)
        dialogBank.setContentView(R.layout.fragment_bank)
        dialogBank.setCanceledOnTouchOutside(false)
        val next = dialogBank.findViewById(R.id.buttonNext)as Button

        dialogBank.btnClose.setOnClickListener {
            dialogBank.dismiss()
        }
       // dialogBank.editAccountName.visibility = View.GONE
       dialogBank.progressBVN.visibility = View.GONE
        if (activity?.getBooleanPreference(R.string.bvn)!!)
            dialogBank.editBVN.visibility = View.GONE

        dialogBank.editAccountName.setTextIsSelectable(false)
        dialogBank.editBVN.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length ==11){
                   val enteredBVN = p0.toString()
                    /*
                    internetCheck = 2
                    InternetCheckAsync().execute()*/

                   // if (activity!!.hasActiveInternetConnection(activity!!)){
                        dialogBank.progressBVN.visibility = View.VISIBLE
                        viewModel.verifyBVN(enteredBVN, activity!!.getIntPreference(R.string.user_id)).observe(this@BanksFragment, Observer {
                            dialogBank.progressBVN.visibility = View.GONE
                        })
                  /*  }else{
                        activity?.showAlert("Please check your Internet connection")
                    }*/

                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        dialogBank.editAccountNo.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length ==10) {
                    accountName(p0.toString(), bankSelected,dialogBank.progressName,dialogBank.editAccountName)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        if (s.equals("EDIT")){
            dialogBank.editAccountNo.setText(acctNumber)
            acctTypeSelected = acctType
            bankSelected = bankName

        }
        viewModel.banks().observe(this, Observer {
            val bankList  = it.data
            val lists = ArrayList<String>()
            lists.add("Select Bank")
            if (bankList != null) {
                for (s in bankList) {
                    lists.add(s?.name.toString())
                }
            }
            val titleAdapter = ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item, lists)
            titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            dialogBank.spinnerBankName.adapter = titleAdapter
            if (bankSelected != null) {
                val selectedPosition = lists.indexOf(bankSelected!!)
                dialogBank.spinnerBankName.setSelection(selectedPosition)

            }

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
        val acctAdapter = ArrayAdapter(activity!!,android.R.layout.simple_spinner_dropdown_item, acctTypeList)
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

        next.setOnClickListener {
            viewModel.addAcct(dialogBank.editAccountNo.text.toString(), bankIdSelected!!,
                activity!!.getIntPreference(R.string.user_id), acctTypeSelected!!).observe(this, Observer {
                dialogBank.relativeProgress.visibility = View.GONE
                if (it.status!!){
                    dialogBank.dismiss()
                    bankList()

                }
            })
        }
        dialogBank.show()
    }
    private fun accountName(
        acctNo: String,
        bankSelected: String?,
        progressName: ProgressBar,
        editAccountName: EditText
    ) {
        progressName.visibility = View.VISIBLE
        viewModel.accountName(acctNo, bankSelected).observe(this@BanksFragment, Observer {
            if (it.status){
                editAccountName.setText(it.data.name)
                progressName.visibility = View.GONE
            }else{
                editAccountName.setText(it.data.name)
            }
        })
    }

    override fun accountDetailsListener(bankName: String?, acctType: String?, acctNumber: String?, s: String?, acctID: String?) {

        if (s.equals("Details")){
            val dialog = Dialog(activity, R.style.Dialog)
            dialog.setContentView(R.layout.fragment_bank_detail)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()

            val bank = dialog.findViewById(R.id.textBank)as TextView
            val actNo = dialog.findViewById(R.id.textAcctNo)as TextView
          //  val textBvn = dialog.findViewById(R.id.textBVN)as TextView
            val actType = dialog.findViewById(R.id.textAcctType)as TextView

            bank.text = bankName
            actNo.text = acctNumber
            //textBvn.text = acctID
            actType.text = acctType
        }else {
            if (s.equals("EDIT")) {
                addBank(s, bankName, acctType, acctNumber)
            }
            if (acctType!!.isNotEmpty()) {

                bankModel = BankModel()
                bankModel!!.bankName = bankName
                bankModel!!.acctType = acctType
                bankModel!!.acctNo = acctNumber
                bankModel!!.acctID = acctID
                listener!!.onBankDetailSubmit(bankModel!!)
                listener!!.onFragmentNavigation(NavigationDirection.BANK_DETAILS_FORWARD)
                adapterListener!!.adapterListener(true)
            } else {
               activity!!.showAlert("Invalid Bank Detail")
            }
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is FragmentListener) {
            listener = parentFragment as FragmentListener
        } /*else {
           // throw RuntimeException(context.toString() + " must implement Loan")
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    companion object {
        var adapterListener : AdapterListener?= null
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: BankModel?/*, param2: String*/) =
            BanksFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    // putString(ARG_PARAM2, param2)
                }
            }
    }
}
