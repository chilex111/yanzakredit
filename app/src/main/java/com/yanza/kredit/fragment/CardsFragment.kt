package com.yanza.kredit.fragment


import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.exceptions.ExpiredAccessCodeException
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.yanza.kredit.activity.DashboardActivity
import com.yanza.kredit.adapter.CardAdapter

import com.yanza.kredit.enums.CardValidity
import com.yanza.kredit.enums.NavigationDirection
import com.yanza.kredit.helper.*
import com.yanza.kredit.listener.*
import com.yanza.kredit.model.CardDetails
import com.yanza.kredit.viwModel.MainViewModel
import fewchore.com.exolve.fewchore.model.CardModel
import kotlinx.android.synthetic.main.fragment_cards.*
import kotlinx.android.synthetic.main.layout_card_details.*
import kotlinx.android.synthetic.main.template_progress.*
import com.yanzu.kredit.R
import kotlinx.android.synthetic.main.content_dashboard.*
import kotlinx.android.synthetic.main.custom_payment_split.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [CardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CardsFragment : Fragment(), PayStackCardValidationListener, CardListListener, StringListener {
    override fun cardDetailsListener(cardDetails: MutableList<CardDetails>?, msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // TODO: Rename and change types of parameters
    private var cardModel: CardModel? = null
    private var payBack: String? = null
    private var loanId: Int? = null
    private var dialog: Dialog? = null
    private var dialogProgress: ProgressDialog? = null
    private var progessPay: ProgressBar? = null
    private var next: Button? = null
    private var cardNo: EditText? = null
    private var expiry: EditText? = null
    private var cvv: EditText? = null
    private var card: Card? = null
    private var expiryDateIsValid = false
    private var cardIsValid = false
    private var cvvIsValid = false
    private var transactionValue: Transaction? = null
    private lateinit var viewModel: MainViewModel

    //  private var paystack_public_key: String?= null
    private var accessCodeValue: String? = null
    private var pageValue: String? = null
    private var cardNoText: String? = null
    private var cardExpiryText: String? = null
    private var cardIdText: String? = null
    private var listener1: LoanPayedListener? = null
    private var listener: FragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CardAdapter.stringlistener = this
        arguments?.let {
            cardModel = it.getParcelable(ARG_PARAM1)
            payBack = it.getString(ARG_PARAM2)
            loanId = it.getInt(ARG_PARAM3)
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
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        /* if (pageValue!= null){
             if(pageValue.equals("1") ||pageValue.equals("2")){
                 cardContainer!!.setBackgroundColor(activity!!.resources.getColor(R.color.green))
                 buttonFrame!!.visibility = View.GONE
             }
         }*/
        cardList()

        addCard.setOnClickListener {
            addCardClicked()
        }
        errorMsg()
    }

    private fun cardList() {
        relativeProgress.visibility = View.VISIBLE
        txt1.visibility = View.GONE
        viewModel.cardList(activity!!.getIntPreference(R.string.user_id))
            .observe(this, androidx.lifecycle.Observer {
                relativeProgress.visibility = View.GONE
                if (it.status!!)
                    if (it.data!!.isNotEmpty()) {
                        txt1.visibility = View.VISIBLE
                        val cardAdapter = CardAdapter(it.data, activity!!, pageValue)
                        recyclerView!!.layoutManager = LinearLayoutManager(activity)

                        recyclerView!!.adapter = cardAdapter
                        recyclerView!!.setHasFixedSize(true)
                        recyclerView!!.requestFocus()
                        // cardAdapter!!.notifyDataSetChanged()
                    } else {
                        textEmpty!!.visibility = View.VISIBLE
                    }
                /*else
                    textEmpty!!.visibility = View.VISIBLE*/


            })
    }

    fun errorMsg() {
        viewModel.error().observe(this, androidx.lifecycle.Observer {
            if (it.contains("API returned error")) {
                activity?.showAlertError("Payment failled", it)
            } else
                activity?.showSnack(it)
            dialogProgress?.dismiss()
            dialog?.dismiss()
            adapterListener!!.adapterListener(true)
        })
    }

    fun addCardClicked() {
        initPayStack()
    }

    private fun initPayStack() {
        relativeProgress.visibility = View.VISIBLE
        viewModel.key().observe(this, androidx.lifecycle.Observer {
            relativeProgress.visibility = View.GONE
            PaystackSdk.setPublicKey(it.publicKey)
            PaystackSdk.initialize(activity)
            addCard()
        })

    }

    override fun accountDetailsListener(
        cardNo: String?,
        cardExpiry: String?,
        cardId: String?,
        s: String?,
        auth_code: String?
    ) {
        if (payBack == null) {
            if (cardNo!!.isNotEmpty()) {

                cardNoText = cardNo
                cardExpiryText = cardExpiry
                cardIdText = cardId

                cardModel = CardModel()
                cardModel!!.cardNo = cardNoText
                cardModel!!.cardId = cardIdText
                cardModel!!.cardExpiry = cardExpiryText
                cardModel!!.authCode = auth_code
                listener!!.onCardDetailSubmit(cardModel!!)
                listener!!.onFragmentNavigation(NavigationDirection.CARD_DETAILS_FORWARD)
            } else {
                activity!!.showAlert("Invalid card Detail")
            }
        } else {
            if (payBack == "0") {
                activity!!.showAlert("You have no Outstanding Loan to pay back")
                adapterListener!!.adapterListener(true)

            } else {
                //  if (buttonFrame!!.visibility == View.GONE) {
                if (!cardNo!!.isEmpty() || loanId != 0) {
                    cardNoText = cardNo
                    cardExpiryText = cardExpiry
                    cardIdText = cardId
                    val msg = "Are you sure you want to pay your loan with this card?"
                    val localBuilder = AlertDialog.Builder(activity!!)
                    localBuilder.setMessage(msg)
                    localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                        // paynow(cardId)
                        paymentPlan(cardId)
                    }
                    localBuilder.create().show()
                } else {
                    activity!!.showAlert("Invalid card Detail")
                }
            }
        }

    }

    private fun paymentPlan(cardId: String?) {
        val msg = "Do you wish to pay some of the loan or all at once"
        val localBuilder = AlertDialog.Builder(activity!!)
        localBuilder.setMessage(msg)
        localBuilder.setNeutralButton(R.string.pay_all) { _, _ ->
            dialogProgress(true)
            payNow(cardId, "")
        }
        localBuilder.setPositiveButton(R.string.pay_part) { _, _ ->
            payPart(cardId)
        }
        localBuilder.create().show()
    }

    private fun payPart(cardId: String?) {
        dialog = Dialog(activity!!)
        dialog!!.setContentView(R.layout.custom_payment_split)
        dialog!!.setCanceledOnTouchOutside(false)


        dialog!!.buttonPay.setOnClickListener {
            if (dialog!!.editAmt.text.toString().isNotEmpty()) {
                dialogProgress(true)
                payNow(cardId, dialog!!.editAmt.text.toString())
            }
        }

        dialog!!.show()
    }

    private fun payNow(cardId: String?, amountToPay: String?) {
        viewModel.payLoan(
            activity!!.getIntPreference(R.string.user_id),
            cardId,
            loanId!!,
            amountToPay
        ).observe(this, androidx.lifecycle.Observer { response ->
            progessPay!!.visibility = View.GONE
            if (response.status!!) {
                dialogProgress(false)
                Log.e("TAG RESPONSE", response.toString())

                if (response.msg == "Payment Failed!") {
                    msgBox(response.msg)
                } else {
                    msgBox(response.msg!!)
                    (activity as DashboardActivity).loanDetail()
                }
                adapterListener!!.adapterListener(true)
            } else {
                msgBox(response.msg!!)
                adapterListener!!.adapterListener(true)
            }

        })
    }

    private fun msgBox(msg: String) {
        val localBuilder = AlertDialog.Builder(activity!!)

        localBuilder.setMessage(msg)
        localBuilder.setNeutralButton(R.string.ok) { _, _ ->
            //listener1!!.onSuccess(msg)

        }
        localBuilder.create().show()
    }

    private fun addCard() {
        dialog = Dialog(activity!!, R.style.Dialog)
        dialog!!.setContentView(R.layout.layout_card_details)
        dialog!!.setCanceledOnTouchOutside(false)
        next = dialog!!.buttonNext
        cardNo = dialog!!.editCardNo
        expiry = dialog!!.editExpiryDate
        cvv = dialog!!.editCVV

        dialog!!.editCardNo.addTextChangedListener(
            CreditCardFormatter(
                CardValidity.CARD_NO,
                this,
                expiry!!,
                card,
                16
            )
        )
        dialog!!.buttonNext.setOnClickListener {
            validateCardForm()
            if (card != null && card!!.isValid) {
                dialogProgress(true)
                viewModel.addCard(activity!!.getIntPreference(R.string.user_id))
                    .observe(this, androidx.lifecycle.Observer {
                        if (it.status!!) {
                            accessCodeValue = it.data?.accessCode
                            try {
                                startAFreshCharge(it.data?.accessCode!!)
                            } catch (e: Exception) {
                                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            activity!!.showAlert("Card cannot be tokenize, Try again !!!")
                        }
                    })


            }
        }

        dialog!!.show()
    }

    private fun dialogProgress(isShow: Boolean) {

        dialogProgress = ProgressDialog(activity)
        dialogProgress!!.setMessage("Performing transaction... please wait")
        dialogProgress!!.setCancelable(true)
        dialogProgress!!.setCanceledOnTouchOutside(true)
        if (!isShow)
            dialogProgress!!.dismiss()
        else
            dialogProgress!!.show()


    }

    private fun startAFreshCharge(accessCode: String) {
        accessCodeValue = accessCode
        val charge = Charge()
        charge.accessCode = accessCodeValue
        charge.email = activity?.getStringPreference(R.string.email)

        charge.card = card
        chargeCard(charge)
    }

    private fun chargeCard(charge: Charge) {
        PaystackSdk.chargeCard(activity, charge, C10894())
    }


    internal inner class C10894 : Paystack.TransactionCallback {

        override fun onSuccess(transaction: Transaction) {
            Log.i("ChargeCard_Success", transaction.reference + " Successful")
            transactionValue = transaction
            viewModel.verifyOnServer(transactionValue!!.reference)
                .observe(activity!!, androidx.lifecycle.Observer {
                    if (it.status!!) {
                        dialogProgress!!.dismiss()
                        dialog!!.dismiss()
                        cardList()
                    } else {
                        activity!!.showAlert("Card cannot be tokenize, Try again !!!")
                    }
                })


        }

        override fun beforeValidate(transaction: Transaction) {}

        override fun onError(error: Throwable, transaction: Transaction) {
            val localBuilder = androidx.appcompat.app.AlertDialog.Builder(activity!!)
            localBuilder.setMessage(error.message)
            localBuilder.setNeutralButton(R.string.ok) { _, _ ->

                if (error is ExpiredAccessCodeException) {
                    startAFreshCharge(accessCodeValue!!)
                }
            }
            localBuilder.create().show()

        }
    }

    private fun validateCardForm() {
        //validate fields
        val cardNum = cardNo!!.text.toString().trim().replace(" ", "")

        if (TextUtils.isEmpty(cardNum)) {
            cardNo!!.error = "Empty card number"
            return
        }

        //build card object with ONLY the number, update the other fields later
        val card = Card.Builder(cardNum, 0, 0, "").build()
        if (!card.validNumber()) {
            cardNo!!.error = "Invalid card number"
            return
        }

        //validate cvc
        val cvc = cvv!!.text.toString().trim()
        if (TextUtils.isEmpty(cvc)) {
            cvv!!.error = "Empty cvc"
            return
        }
        //update the cvc field of the card
        card.cvc = cvc

        //check that it's valid
        if (!card.validCVC()) {
            cvv!!.error = "Invalid cvc"
            return
        }

        val date = expiry!!.text.toString()
        if (date.contains("/")) {
            val rawYear = Calendar.getInstance().get(Calendar.YEAR).toString()
            val yearPrefix = rawYear.substring(0, 2)
            val monthYear = date.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val monthStr = monthYear[0]
            val yearStr = yearPrefix + monthYear[1]

            var month = -1
            try {
                month = Integer.parseInt(monthStr)
            } catch (ignored: Exception) {
            }

            if (month in 1..12) {
                card.expiryMonth = month
            } else {
                return
            }

            var year = -1
            try {
                year = Integer.parseInt(yearStr)
            } catch (ignored: Exception) {
            }

            if (year > 0) {
                card.expiryYear = year
            } else {
                return
            }

            if (!card.validExpiryDate()) {
                expiry!!.error = "Invalid expiry"
            }
        }
    }

    override fun afterChange(cardValidity: CardValidity, editable: Editable) {
        when (cardValidity) {
            CardValidity.EXPIRY_DATE -> {
                if (editable.length == 2) {
                    var month = -1
                    try {
                        month = Integer.parseInt(editable.toString().trim({ it <= ' ' }))
                    } catch (e: Exception) {
                    }

                    if (month < 1 || month > 12) {
                        expiry!!.error = "Invalid month"
                    }
                    if (editable.length == 5 && !editable.toString().contains("/")) {
                        expiry!!.error = "Invalid date"
                        return
                    }
                    return
                }
                return
            }
            else -> {
            }
        }

    }

    override fun paramIsValid(z: Boolean, cardValidity: CardValidity) {
        when (cardValidity) {
            CardValidity.CARD_NO -> {
                if (z) {
                    card = Card.Builder(
                        this.cardNo!!.text.toString().trim().replace(" ", ""),
                        Integer.valueOf(0),
                        Integer.valueOf(0),
                        ""
                    ).build()
                    expiry!!.addTextChangedListener(
                        CreditCardFormatter(
                            CardValidity.EXPIRY_DATE,
                            this,
                            this.cvv!!,
                            card,
                            5
                        )
                    )
                    cvv!!.addTextChangedListener(
                        CreditCardFormatter(
                            CardValidity.CVV,
                            this,
                            cardNo!!,
                            card,
                            3
                        )
                    )
                    expiry!!.visibility = View.VISIBLE
                    cvv!!.visibility = View.VISIBLE
                    next!!.visibility = View.VISIBLE
                    cardIsValid = true
                    checkCardValidity()
                    return
                }
                cardNo!!.error = "Invalid card number"
                return
            }
            CardValidity.EXPIRY_DATE -> {
                if (z) {
                    expiryDateIsValid = true
                    checkCardValidity()
                    return
                }
                expiry!!.error = "Invalid expiry date"
                return
            }
            CardValidity.CVV -> {
                if (z) {
                    cvvIsValid = true
                    checkCardValidity()
                    return
                }
                cvv!!.error = "Invalid cvc"
                return
            }
            else -> return
        }
    }

    private fun checkCardValidity() {
        if (this.cardIsValid && expiryDateIsValid && cvvIsValid) {
            next!!.isEnabled = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (parentFragment is FragmentListener) {
            listener = parentFragment as FragmentListener
        } else {
            //throw RuntimeException("$context must implement Loan")
        }
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        var adapterListener: AdapterListener? = null

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: CardModel?, param2: String?, param3: Int?) =
            CardsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    if (param3 != null) {
                        putInt(ARG_PARAM3, param3)
                    }

                }
            }
    }
}
