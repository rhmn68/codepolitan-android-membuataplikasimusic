package com.codepolitan.musicapp.views.user

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codepolitan.musicapp.R
import com.codepolitan.musicapp.databinding.FragmentUserBinding
import com.codepolitan.musicapp.views.changepassword.ChangePasswordActivity
import com.codepolitan.musicapp.views.edituser.EditUserActivity
import com.codepolitan.musicapp.views.login.LoginActivity
import com.codepolitan.musicapp.views.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val userBinding get() = _binding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Init
        auth = FirebaseAuth.getInstance()
        onClick()
    }

    private fun onClick() {
        userBinding?.btnEditUser?.setOnClickListener {
            context?.startActivity<EditUserActivity>()
        }

        userBinding?.btnChangeLanguage?.setOnClickListener {
            startActivity(Intent(ACTION_LOCALE_SETTINGS))
        }

        userBinding?.btnChangePassword?.setOnClickListener {
            context?.startActivity<ChangePasswordActivity>()
        }

        userBinding?.btnLogout?.setOnClickListener {
            auth.signOut()
            context?.startActivity<LoginActivity>()
            (activity as MainActivity).finishAffinity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}