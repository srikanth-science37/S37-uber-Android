package com.mp.poc.s37uberandroid.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.mp.poc.s37uberandroid.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var isPasswordEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initClickListeners()
    }

    private fun initClickListeners() {
        btLogin.setOnClickListener {
            goToDashboard()
        }

        btPasswordText.setOnClickListener {
            passwordAction()
        }

        tvRegisterNow.setOnClickListener {
            goToRegistration()
        }
    }

    private fun goToDashboard() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun goToRegistration() {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }

    private fun passwordAction() {
        if (isPasswordEnabled) {
            etPassword.inputType = InputType.TYPE_CLASS_TEXT
            etPassword.setSelection(if (etPassword.text?.isNotBlank() == true) etPassword.text?.length!! else 0)
            etPassword.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_universal_view_disabled,
                0
            )
            isPasswordEnabled = false
        } else {
            //TODO Need to check why value 129 works for password char, even though there is no such constant with this value.
            etPassword.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD + InputType.TYPE_CLASS_TEXT
            etPassword.setSelection(if (etPassword.text?.isNotBlank() == true) etPassword.text?.length!! else 0)
            etPassword.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_universal_view,
                0
            )
            isPasswordEnabled = true
        }
    }
}