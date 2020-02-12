package com.yanza.kredit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yanzu.kredit.R
import com.yanza.kredit.fragment.BanksFragment
import com.yanza.kredit.listener.AdapterListener
import com.yanza.kredit.listener.StringListener
import com.yanza.kredit.model.AcctListData

class BankAdapter(private val bankModelList: List<AcctListData>?, private val context: Context, private val pageValue: String?)
    : RecyclerView.Adapter<BankAdapter.BankViewHolder>() , AdapterListener {

    companion object {
        var stringlistener: StringListener? = null
        var cardPosition: Int? = -1
        var viewHolder: BankViewHolder? = null
    }

    class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView? = null
        var textBankName: TextView? = null
        var textAcctNo: TextView? = null
        var textAcctType: TextView?= null
        var buttonEdit: Button ?= null

        init {
            cardView = itemView.findViewById(R.id.cardBank)
            textBankName = itemView.findViewById(R.id.textBank)
            textAcctNo = itemView.findViewById(R.id.textAcctNo)
            textAcctType = itemView.findViewById(R.id.textAcctType)
            buttonEdit = itemView.findViewById(R.id.buttonEdit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.custom_bank, parent, false)
        BanksFragment.adapterListener = this
        return BankViewHolder(v)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        viewHolder = holder
        val name = bankModelList!![position].name
        val type = bankModelList[position].accttype
        val number = bankModelList[position].accno

        holder.textBankName!!.text = name
        holder.textAcctNo!!.text = number
        holder.textAcctType!!.text = type

        try {
            holder.cardView!!.setOnClickListener {
                if (pageValue == "2") {
                    stringlistener!!.accountDetailsListener(name, type, number, "Details",bankModelList[position].id.toString())
                } else {
                    cardPosition = position
                    notifyDataSetChanged()

                if (cardPosition == position) {
                    holder.cardView!!.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    stringlistener!!.accountDetailsListener(name, type, number, null, bankModelList[position].id.toString())
                } else {
                    holder.cardView!!.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            }
        }catch (e: Exception){
           // Log.i("BANK_TAG", e.message)
        }
        holder.buttonEdit!!.setOnClickListener{
            val string="EDIT"
            stringlistener!!.accountDetailsListener(bankModelList[position].name,
                bankModelList[position].accttype, bankModelList[position].accno
                , string, bankModelList[position].id.toString())
        }
    }

    override fun getItemCount(): Int {
        return bankModelList!!.size
    }

    override fun adapterListener(status: Boolean) {
        if (status){
            viewHolder!!.cardView!!.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

}

