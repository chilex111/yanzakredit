package com.yanza.kredit.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanzu.kredit.R
import com.yanza.kredit.adapter.LoanHistoryAdapter
import com.yanza.kredit.helper.getIntPreference
import com.yanza.kredit.helper.showSnack
import com.yanza.kredit.listener.GenericListener
import com.yanza.kredit.model.History
import com.yanza.kredit.viwModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.template_progress.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HistoryFragment : Fragment(),GenericListener<History> {
    override fun genericListener(genericModel: History) {
        if (genericModel.status!!.contains("awaiting", true))
            viewModel.deletLoan(activity?.getIntPreference(R.string.user_id)!!, genericModel.id!!).observe(this, Observer {
                if (it.status!!){
                    historyLoad()
                }
                else
                    activity!!.showSnack(it.msg.toString())
            })
    }

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoanHistoryAdapter.deleteLoan =this
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        historyLoad()
        loadError()
    }

    private fun historyLoad(){
        relativeProgress.visibility = View.VISIBLE
        viewModel.history(activity?.getIntPreference(R.string.user_id)!!).observe(this, Observer {
            relativeProgress.visibility = View.GONE
            if (it.status!!&& (!it.data.isNullOrEmpty())) {
                linear.visibility = View.VISIBLE
                emptyTxt!!.visibility =View.GONE
                val audioAdapter = LoanHistoryAdapter(it.data, this.activity!!)
                recyclerView!!.layoutManager = LinearLayoutManager(activity)
                recyclerView!!.adapter = audioAdapter
                recyclerView!!.requestFocus()
            }
            else{
                linear.visibility = View.GONE
                emptyTxt!!.visibility =View.VISIBLE
            }
        })
    }
    private fun loadError(){
        viewModel.error().observe(this, Observer {
            activity!!.showSnack(it)
        })
    }

}
