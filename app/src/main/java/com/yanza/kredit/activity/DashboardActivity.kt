package com.yanza.kredit.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.yanzu.kredit.R
import com.yanza.kredit.apis.GetInterestAsync
import com.yanza.kredit.apis.LoanLimitAsync
import com.yanza.kredit.fragment.*
import com.yanza.kredit.model.HistoryModel
import com.yanza.kredit.viwModel.MainViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboard.view.*
import java.text.DecimalFormat
import com.bumptech.glide.request.RequestOptions
import com.yanza.kredit.helper.*

@SuppressLint("SetTextI18n")
class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private lateinit var viewModel: MainViewModel
    private var loanId: Int?= null
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnHome ->{
                txt(
                    R.color.green,
                    R.color.grey,
                    R.color.grey,
                    R.color.grey
                )

                loanDetail()
                firstTimer()
            }
            R.id.btnLoan ->{
                loan()
            }
            R.id.btnPay ->{
                pay()
            }
            R.id.btnProfile ->{
                profile()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        firstTimer()
        loanDetail()
        btnHome.setOnClickListener(this)
        btnLoan.setOnClickListener(this)
        btnPay.setOnClickListener(this)
        btnProfile.setOnClickListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.setHomeAsUpIndicator(R.drawable.ic_nav)
        toggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_nav)
        nav_view.setNavigationItemSelectedListener(this)

        val headerview = layoutInflater.inflate(R.layout.nav_header_dashboard, nav_view, false)
        nav_view.addHeaderView(headerview)

        val headerView: View = nav_view.getHeaderView(0)
        val name = getStringPreference(R.string.first_name)+" "+getStringPreference(R.string.surname)
        headerView.txtName.text = name //"${getStringPreference(R.string.first_name)} ${getStringPreference(R.string.surname)}"

        Glide.with(this)
            .load(getStringPreference(R.string.profile))
            .apply(RequestOptions.circleCropTransform().error(R.mipmap.placeholder))
            .into( headerView.imageView)

        btnLogOut.setOnClickListener {
            logout(this)
        }
    }

    fun loanDetail(){
        GetInterestAsync().execute()
        LoanLimitAsync(getIntPreference(R.string.user_id)).execute()
        viewModel.loanDetail(getIntPreference(R.string.user_id)).observe(this, Observer {
            if (it.status!!) {
                val formatter = DecimalFormat("#,###,###.##")
                loanId = it.data?.loanId
                val amount = formatter.format(it.data?.amount!!.toDouble())
                txtAmt.text ="₦$amount"
                txtName.text = getStringPreference(R.string.first_name).capitalize()
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                profile()
            }
            R.id.nav_loan -> {
                loan()
            }
            R.id.nav_payback -> {
                pay()
            }
            R.id.nav_history -> {
                createFragment2(HistoryFragment())
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun firstTimer(){
        clearView()
        viewModel.history(getIntPreference(R.string.user_id)).observe(this, Observer {
            if (it.status!!)
                if (it.data!!.isNotEmpty()){
                    createFragment(HistoryFragment())
                }
                else
                    createFragment(FirstTimeFragment())

        })
        txt(
            R.color.green,
            R.color.grey,
            R.color.grey,
            R.color.grey
        )
    }
    private fun history(){
        clearView()
        createFragment(FirstTimeFragment())
        txt(
            R.color.green,
            R.color.grey,
            R.color.grey,
            R.color.grey
        )
    }

    fun loan(){
        clearView()
        createFragment(LoanDetailFragment())
        txt(
            R.color.grey,
            R.color.green,
            R.color.grey,
            R.color.grey
        )
    }

    private fun pay(){
        val loanAmt = txtAmt!!.text.toString()
        if(loanAmt != "0.00") {
            clearView()
            val id = loanId

            createFragment(CardsFragment.newInstance(null, loanAmt, id))
            txt(
                R.color.grey,
                R.color.grey,
                R.color.green,
                R.color.grey
            )
        }
        else{
            showAlert("You have no outstanding loan")
        }
    }

    private fun profile(){
        clearView()
        createFragment1(ProfileFragment())
        txt(
            R.color.grey,
            R.color.grey,
            R.color.grey,
            R.color.green
        )
    }

    private fun clearView(){
        frameContainer.removeAllViews()
        container1.removeAllViews()
        container2.removeAllViews()
    }

    private fun createFragment(fragmentClass: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragmentClass)
            .addToBackStack(null)
            .commit()
    }
    private fun createFragment1(fragmentClass: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container1, fragmentClass)
            .addToBackStack(null)
            .commit()
    }
    private fun createFragment2(fragmentClass: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container2, fragmentClass)
            .addToBackStack(null)
            .commit()
    }

    fun txt(first:Int , second:Int, third: Int, fourth: Int){
        setTextViewDrawableColor(btnHome, first)
        setTextViewDrawableColor(btnLoan, second)
        setTextViewDrawableColor(btnPay, third)
        setTextViewDrawableColor(btnProfile, fourth)

        btnHome.setTextColor(first)
        btnLoan.setTextColor(second)
        btnPay.setTextColor(third)
        btnProfile.setTextColor(fourth)
    }
}
