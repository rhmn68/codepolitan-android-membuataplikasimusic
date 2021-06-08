package com.codepolitan.musicapp.views.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.codepolitan.musicapp.R
import com.codepolitan.musicapp.databinding.ActivityLoginBinding
import com.codepolitan.musicapp.utils.gone
import com.codepolitan.musicapp.utils.visible
import com.codepolitan.musicapp.views.forgotpassword.ForgotPasswordActivity
import com.codepolitan.musicapp.views.main.MainActivity
import com.codepolitan.musicapp.views.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance()
        checkIfAlreadyLogin()
        onClick()
    }

    private fun checkIfAlreadyLogin() {
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity<MainActivity>()
            finishAffinity()
        }
    }

    private fun onClick() {
        loginBinding.btnForgotPassword.setOnClickListener {
            startActivity<ForgotPasswordActivity>()
        }

        loginBinding.btnNewRegister.setOnClickListener {
            startActivity<RegisterActivity>()
        }

        loginBinding.btnLogin.setOnClickListener {
            val email = loginBinding.etEmail.text.toString().trim()
            val pass = loginBinding.etPassword.text.toString().trim()

            if (checkValidation(email, pass)){
                loginToServer(email, pass)
            }
        }
    }

    private fun loginToServer(email: String, pass: String) {
        showLoading()
        auth.signInWithEmailAndPassword(email, pass)
          .addOnSuccessListener {
              hideLoading()
              startActivity<MainActivity>()
              finishAffinity()
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
        loginBinding.pbLogin.gone()
    }

    private fun showLoading() {
        loginBinding.pbLogin.visible()
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        if (email.isEmpty()){
            loginBinding.etEmail.error = getString(R.string.please_field_your_email)
            loginBinding.etEmail.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginBinding.etEmail.error = getString(R.string.please_use_valid_email)
            loginBinding.etEmail.requestFocus()
        }else if (pass.isEmpty()){
            loginBinding.etPassword.error = getString(R.string.please_field_your_password)
            loginBinding.etPassword.requestFocus()
        }else{
            return true
        }
        return false
    }
}