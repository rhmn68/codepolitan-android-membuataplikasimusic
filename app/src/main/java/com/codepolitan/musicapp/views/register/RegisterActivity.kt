package com.codepolitan.musicapp.views.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.codepolitan.musicapp.R
import com.codepolitan.musicapp.databinding.ActivityRegisterBinding
import com.codepolitan.musicapp.models.User
import com.codepolitan.musicapp.utils.gone
import com.codepolitan.musicapp.utils.visible
import com.codepolitan.musicapp.views.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        init()
        onClick()
    }

    private fun onClick() {
        registerBinding.tbRegister.setNavigationOnClickListener {
            finish()
        }

        registerBinding.btnAlreadyMemberLogin.setOnClickListener {
            finish()
        }

        registerBinding.btnRegister.setOnClickListener {
            val fullName = registerBinding.etFullName.text.toString().trim()
            val email = registerBinding.etEmail.text.toString().trim()
            val pass = registerBinding.etPassword.text.toString().trim()
            val confirmPass = registerBinding.etConfirmPassword.text.toString().trim()

            if (checkValidation(fullName, email, pass, confirmPass)){
                registerToServer(fullName, email, pass)
            }
        }
    }

    private fun registerToServer(fullName: String, email: String, pass: String) {
        showLoading()
        auth.createUserWithEmailAndPassword(email, pass)
          .addOnCompleteListener { authResult ->
              if (authResult.isSuccessful){
                  val uid = auth.currentUser?.uid
                  val user = User(fullName = fullName, email = email, uid = uid)

                  userDatabase.child(uid.toString()).setValue(user)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            hideLoading()
                            AlertDialog.Builder(this)
                              .setTitle(getString(R.string.success))
                              .setMessage(getString(R.string.your_account_has_been_created))
                              .show()

                            Handler(Looper.getMainLooper()).postDelayed({
                                startActivity<LoginActivity>()
                            }, 1500)
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
        registerBinding.pbRegister.gone()
    }

    private fun showLoading() {
        registerBinding.pbRegister.visible()
    }

    private fun checkValidation(fullName: String, email: String, pass: String, confirmPass: String): Boolean {
        if (fullName.isEmpty()){
            registerBinding.etFullName.error = getString(R.string.please_field_your_full_name)
            registerBinding.etFullName.requestFocus()
        }else if (email.isEmpty()){
            registerBinding.etEmail.error = getString(R.string.please_field_your_email)
            registerBinding.etEmail.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerBinding.etEmail.error = getString(R.string.please_use_valid_email)
            registerBinding.etEmail.requestFocus()
        }else if (pass.isEmpty()){
            registerBinding.etPassword.error = getString(R.string.please_field_your_password)
            registerBinding.etPassword.requestFocus()
        }else if (confirmPass.isEmpty()){
            registerBinding.etConfirmPassword.error = getString(R.string.please_field_your_confirm_password)
            registerBinding.etConfirmPassword.requestFocus()
        }else if (pass != confirmPass){
            registerBinding.etPassword.error = getString(R.string.your_password_didnt_match)
            registerBinding.etPassword.requestFocus()
            registerBinding.etConfirmPassword.error = getString(R.string.your_password_didnt_match)
            registerBinding.etConfirmPassword.requestFocus()
        }else{
            return true
        }
        return false
    }

    private fun init() {
        //Setup Support Action Bar
        setSupportActionBar(registerBinding.tbRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        auth = FirebaseAuth.getInstance()
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
    }
}