package com.codepolitan.musicapp.views.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.codepolitan.musicapp.R
import com.codepolitan.musicapp.databinding.ActivityForgotPasswordBinding
import com.codepolitan.musicapp.utils.gone
import com.codepolitan.musicapp.utils.visible
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(forgotPasswordBinding.root)

        init()
        onClick()
    }

    private fun onClick() {
        forgotPasswordBinding.tbForgotPassword.setNavigationOnClickListener {
            finish()
        }

        forgotPasswordBinding.btnForgotPassword.setOnClickListener {
            val email = forgotPasswordBinding.etEmail.text.toString().trim()

            if (checkValidation(email)){
                forgotPasswordToServer(email)
            }
        }
    }

    private fun forgotPasswordToServer(email: String) {
        showLoading()
        auth.sendPasswordResetEmail(email)
          .addOnCompleteListener {
              if (it.isSuccessful){
                  hideLoading()
                  AlertDialog.Builder(this)
                    .setTitle(getString(R.string.success))
                    .setMessage(getString(R.string.link_for_reset_password_has_been_sent_to_your_email))
                    .show()

                  Handler(Looper.getMainLooper()).postDelayed({
                      finish()
                  }, 1400)
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
        forgotPasswordBinding.pbForgotPassword.gone()
    }

    private fun showLoading() {
        forgotPasswordBinding.pbForgotPassword.visible()
    }

    private fun checkValidation(email: String): Boolean {
        if (email.isEmpty()){
            forgotPasswordBinding.etEmail.error = getString(R.string.please_field_your_email)
            forgotPasswordBinding.etEmail.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            forgotPasswordBinding.etEmail.error = getString(R.string.please_use_valid_email)
            forgotPasswordBinding.etEmail.requestFocus()
        }else{
            return true
        }
        return false
    }

    private fun init() {
        //Set Support ActionBar
        setSupportActionBar(forgotPasswordBinding.tbForgotPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        auth = FirebaseAuth.getInstance()
    }
}