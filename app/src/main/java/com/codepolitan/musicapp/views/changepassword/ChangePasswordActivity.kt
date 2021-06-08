package com.codepolitan.musicapp.views.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.codepolitan.musicapp.R
import com.codepolitan.musicapp.databinding.ActivityChangePasswordBinding
import com.codepolitan.musicapp.utils.gone
import com.codepolitan.musicapp.utils.visible
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.toast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        init()
        onClick()
    }

    private fun onClick() {
        changePasswordBinding.tbChangePassword.setNavigationOnClickListener {
            finish()
        }

        changePasswordBinding.btnUpdate.setOnClickListener {
            val oldPass = changePasswordBinding.etOldPassword.text.toString().trim()
            val newPass = changePasswordBinding.etNewPassword.text.toString().trim()

            if (checkValidation(oldPass, newPass)){
                changePasswordToServer(oldPass, newPass)
            }
        }
    }

    private fun changePasswordToServer(oldPass: String, newPass: String) {
        showLoading()
        val credential = EmailAuthProvider.getCredential(currentUser.email.toString(), oldPass)
        currentUser.reauthenticate(credential)
          .addOnSuccessListener {
              currentUser.updatePassword(newPass)
                .addOnSuccessListener {
                    hideLoading()
                    AlertDialog.Builder(this)
                      .setTitle(getString(R.string.success))
                      .setMessage(getString(R.string.your_password_has_been_changed))
                      .show()

                    Handler(Looper.getMainLooper())
                      .postDelayed({
                          finish()
                      }, 2000)
                }
                .addOnFailureListener {
                    hideLoading()
                    AlertDialog.Builder(this)
                      .setTitle(getString(R.string.error))
                      .setMessage(it.message)
                      .show()
                }
          }
          .addOnFailureListener {
              hideLoading()
              AlertDialog.Builder(this)
                .setTitle(getString(R.string.error))
                .setMessage(it.message)
                .show()
          }
    }

    private fun hideLoading() {
        changePasswordBinding.pbChangePassword.gone()
    }

    private fun showLoading() {
        changePasswordBinding.pbChangePassword.visible()
    }

    private fun checkValidation(oldPass: String, newPass: String): Boolean {
        if (oldPass.isEmpty()){
            changePasswordBinding.etOldPassword.error = getString(R.string.please_field_your_password)
            changePasswordBinding.etOldPassword.requestFocus()
        }else if (newPass.isEmpty()){
            changePasswordBinding.etNewPassword.error = getString(R.string.please_field_your_password)
            changePasswordBinding.etNewPassword.requestFocus()
        }else{
            return true
        }
        return false
    }

    private fun init() {
        //Set Support ActionBar
        setSupportActionBar(changePasswordBinding.tbChangePassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        currentUser = FirebaseAuth.getInstance().currentUser!!
    }
}