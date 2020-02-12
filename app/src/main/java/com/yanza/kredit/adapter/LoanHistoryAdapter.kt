package com.yanza.kredit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yanza.kredit.listener.GenericListener
import com.yanzu.kredit.R
import com.yanza.kredit.model.History
import java.text.DecimalFormat




class LoanHistoryAdapter(private val historyModelList: List<History>?, private val context: Context) : RecyclerView.Adapter<LoanHistoryAdapter.HomeViewHolder>() {

companion object{
    var deleteLoan: GenericListener<History>?= null
}
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView : CardView = itemView.findViewById(R.id.cardHolder)
        var amount: TextView = itemView.findViewById(R.id.textAmount)
        val duration: TextView = itemView.findViewById(R.id.textDuration)
        val status: TextView = itemView.findViewById(R.id.textStatus)
        var interest : TextView = itemView.findViewById(R.id.textInterest)
        var btnMore : ImageButton = itemView.findViewById(R.id.btnDelete)


    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(paramViewGroup: ViewGroup, paramInt: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(paramViewGroup.context)
                .inflate(R.layout.custom_loan_history, paramViewGroup, false))
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val decimalFormat = DecimalFormat("#,###.##")
        val am = decimalFormat.format(java.lang.Double.parseDouble(historyModelList!![position].amount.toString()))
        holder.amount.text = am
        holder.interest.text= historyModelList[position].interest.toString()
        holder.duration.text= historyModelList[position].paybackdate.toString()
        val input =  historyModelList[position].status
        if (!input.isNullOrEmpty()) {
            val output = input.substring(0, 1).toUpperCase() + input.substring(1)
            when {
                historyModelList[position].status.equals("reject",true) -> {
                    holder.status.text = context.getString(R.string.rejected)
                }
                historyModelList[position].status.equals("disbursed", true)|| historyModelList[position].status.equals("approve", true)-> {
                    holder.btnMore.visibility = View.GONE
                    holder.status.text = context.getString(R.string.active)
                    holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_purple))
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_purple))
                }
                historyModelList[position].status .equals("paid", true)/*||(historyModelList[position].status .equals("approved", true)) */-> {
                    holder.btnMore.visibility = View.GONE
                    holder.status.text = context.getString(R.string.paid)
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.green))
                    holder.cardView.setCardBackgroundColor(Color.GREEN)
                }
                historyModelList[position].status!!.contains("awaiting", true) -> {
                    holder.status.text =  historyModelList[position].status
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.dark))
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.dark))
                }
                historyModelList[position].status!!.contains("cancel", true) -> {
                    holder.btnMore.visibility = View.GONE
                    holder.status.text =  historyModelList[position].status
                }
                else -> holder.status.text = output
            }
        }

        holder.btnMore.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(context, holder.btnMore)
            //inflating menu from xml resource
            popup.inflate(R.menu.delete)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                       deleteLoan?.genericListener(historyModelList[position])
                    }

                }
                false
            }
            //displaying the popup
            popup.show()
        }

    }

    override fun getItemCount(): Int {
        return historyModelList!!.size
    }




}
