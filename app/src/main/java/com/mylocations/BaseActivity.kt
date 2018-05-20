package com.mylocations

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.mylocations.detail.LocationDetailActivity
import com.mylocations.utils.Config

/**
 * Abstract class to display progress dialog and for toolbar home button functionality
 */
abstract class BaseActivity : AppCompatActivity() {

    internal var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            progressDialog = createProgressDialog()
        } catch (e: Exception) {

        }
    }

    protected fun closeProgress() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {

        }
    }

    protected fun showProgress() {
        try {
            if (progressDialog != null) {
                progressDialog!!.show()
            }
        } catch (e: Exception) {

        }
    }

    protected fun createProgressDialog(): ProgressDialog? {
        var progressDialog: ProgressDialog? = null
        try {
            progressDialog = ProgressDialog(this)
            progressDialog.isIndeterminate = false
            progressDialog.setMessage("Loading...")
            progressDialog.setCancelable(false)
        } catch (e: Exception) {

        }
        return progressDialog
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            closeProgress()
        } catch (e: Exception) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun startLocationDetailActivity(id: Int){
        val intent = Intent(this, LocationDetailActivity::class.java)
        intent.putExtra(Config.EXTRA_LOCATION_ID, id)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
