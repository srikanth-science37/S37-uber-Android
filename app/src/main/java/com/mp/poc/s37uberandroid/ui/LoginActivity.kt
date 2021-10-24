package com.mp.poc.s37uberandroid.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.utils.Preference
import com.mp.poc.s37uberandroid.utils.Utils
import com.mp.poc.s37uberandroid.utils.Utils.toEditable
import com.mp.poc.s37uberandroid.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : FragmentActivity() {

    private var isPasswordEnabled = true
    private var loginTries = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initClickListeners()
        initTextUpdateListeners()
    }

    override fun onResume() {
        super.onResume()

        if (Preference.isSignUp(this) || Preference.isLogin(this)) {
            val userEmailId = Preference.getEmailId(this)
            userEmailId?.apply {
                etEmail.text = this.toEditable()
            }
        }
    }

    private fun initViews() {
        etEmail.setSingleLine()
        etPassword.setSingleLine()
    }

    private fun initClickListeners() {
        btLogin.setOnClickListener {
            loginButtonAction()
        }

        btPasswordText.setOnClickListener {
            passwordAction()
        }

        tvRegisterNow.setOnClickListener {
            goToRegistration()
        }
    }

    private fun initTextUpdateListeners() {
        etEmail.addTextChangedListener {
            tvErrorMessage.visibility = View.GONE

            it?.apply {
                if (this.toString().isNotBlank() && etPassword.length() > 0) {
                    buttonEnableAction(true)
                } else {
                    buttonEnableAction(false)
                }
            }
        }

        etPassword.addTextChangedListener {
            tvErrorMessage.visibility = View.GONE

            it?.apply {
                if (this.toString().isNotBlank() && etEmail.length() > 0) {
                    buttonEnableAction(true)
                } else {
                    buttonEnableAction(false)
                }
            }
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

    private fun loginButtonAction() {
        val email = etEmail.text?.toString()!!
        val password = etPassword.text?.toString()!!

        if (!Utils.isValidEmail(email)) {
            loginTries = 0
            S37DialogFragment(resources.getString(R.string.email_error_message)).show(
                supportFragmentManager,
                "DialogFragment"
            )
        } else if (!Preference.isSignUp(this) || !isEmailMatches(email) ||
            !isPasswordMatches(password)
        ) {
            loginTries++
            tvErrorMessage.visibility = View.VISIBLE
            if (loginTries in 3..5) {
                val warningMessage = resources.getString(R.string.error_message_wrong_cred_warning)
                tvErrorMessage.text = warningMessage.replace("[#]", "${6 - loginTries}")
            } else if (loginTries == 6) {
                tvErrorMessage.text = resources.getString(R.string.account_locked_message)
                btLogin.isEnabled = false
                btLogin.isClickable = false
            }
        } else {
            loginTries = 0
            tvErrorMessage.visibility = View.GONE
            Preference.putIsLogin(this, true)
            goToDashboard()
        }
    }

    private fun isEmailMatches(emailToValidate: String): Boolean =
        emailToValidate == Preference.getEmailId(this)

    private fun isPasswordMatches(passwordToValidate: String): Boolean =
        passwordToValidate == Preference.getPassword(this)

    private fun buttonEnableAction(enable: Boolean) {
        ViewUtils.setButtonAlpha(this, btLogin, enable)
    }
}