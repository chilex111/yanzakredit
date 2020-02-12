package com.yanza.kredit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yanzu.kredit.R
import com.yanza.kredit.fragment.CardsFragment
import com.yanza.kredit.listener.AdapterListener
import com.yanza.kredit.listener.StringListener
import com.yanza.kredit.model.CardsData

class CardAdapter(private val cardModelList: List<CardsData>, private val context: Context, private val pageValue: String?)
    : RecyclerView.Adapter<CardAdapter.CardViewHolder>() , AdapterListener {

    companion object {
        var stringlistener: StringListener? = null
        var cardPosition: Int? = -1
        @SuppressLint("StaticFieldLeak")
        var viewHolder: CardViewHolder? = null

    }


    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.findViewById(R.id.container)
        var textNo: TextView ?= null
        val textExpiry:TextView
        val textCVV: TextView
        val cardType : TextView


        init {
            this.textNo = itemView.findViewById(R.id.textCardNo)
            this.textExpiry = itemView.findViewById(R.id.textExpiry)
            this.textCVV = itemView.findViewById(R.id.textCardCVV)
            cardType = itemView.findViewById(R.id.textCardType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.custom_card, parent, false)
        CardsFragment.adapterListener = this

        return CardViewHolder(v)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        viewHolder = holder

        val cardNum = "**** **** **** "+cardModelList[position].number
        val cardCVV =  "***"
        val split =cardModelList[position].expiryYear?.substring(2)
        val cardExp =  "**/$split"
        val cardId = cardModelList[position].id
        val card_type = cardModelList[position].bank
        val auth_code = cardModelList[position].authCode

        holder.textNo!!.text = cardNum
        holder.textCVV.text =cardCVV
        holder.textExpiry.text= cardExp
       holder.cardType.text = card_type


        try {
            holder.cardView.setOnClickListener {
                if (pageValue =="2"){

                }else {
                    cardPosition = position
                    notifyDataSetChanged()

                    if (cardPosition == position) {
                        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                        stringlistener!!.accountDetailsListener(cardNum, cardExp, cardId.toString(), "ADD", auth_code)
                    } else {
                        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    }
                }
            }
        }catch (e: Exception){
            Log.i("CARD_TAG", e.message)
        }

    }

    override fun getItemCount(): Int {
        return cardModelList.size
    }

    override fun adapterListener(status: Boolean) {
        if (status){
            viewHolder!!.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

}

