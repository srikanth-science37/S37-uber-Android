package com.mp.poc.s37uberandroid.ui

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.model.SignUpInputFieldModel
import com.mp.poc.s37uberandroid.ui.adapter.SignUpInputFieldRVAdapter
import com.mp.poc.s37uberandroid.utils.Preference
import com.mp.poc.s37uberandroid.utils.Utils
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : FragmentActivity(),
    SignUpInputFieldRVAdapter.OnSignUpScreenUpdateListener, S37DialogFragment.S37DialogListener {

    private lateinit var filledData: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initClickListeners()
        loadSignUpFields()
    }

    private fun initClickListeners() {
        tvSignIn.setOnClickListener {
            goBackToSignIn()
        }

        btSignUp.setOnClickListener {
            validateFields()
        }
    }

    private fun goBackToSignIn() {
        finish()
    }

    private fun loadSignUpFields() {
        val data = mutableListOf<SignUpInputFieldModel>()
        val allFields = resources.getStringArray(R.array.signup_fields_array)

        for (item in allFields) {
            data.add(SignUpInputFieldModel(item))
        }

        val dividerItemDecoration = DividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.home_recycler_view_divider,
                null
            )!!
        )

        inputFieldRecyclerView.addItemDecoration(dividerItemDecoration)
        inputFieldRecyclerView.adapter = SignUpInputFieldRVAdapter(data, this)
    }

    override fun onInputFieldsUpdate(shouldEnableSignUpButton: Boolean, allData: Array<String>) {
        filledData = allData

        if (shouldEnableSignUpButton) {
            btSignUp.alpha = 1f
            btSignUp.setTextColor(resources.getColor(R.color.white, null))
            btSignUp.isEnabled = true
            btSignUp.isClickable = true
        } else {
            btSignUp.alpha = 0.3f
            btSignUp.setTextColor(resources.getColor(R.color.black, null))
            btSignUp.isEnabled = false
            btSignUp.isClickable = false
        }
    }

    private fun validateFields() {
        if (!Utils.isValidEmail(filledData[2])) {
            S37DialogFragment(resources.getString(R.string.email_error_message)).show(
                supportFragmentManager,
                "DialogFragment"
            )
        } else if (!Utils.isValidPassword(filledData[4])) {
            S37DialogFragment(resources.getString(R.string.password_error_message)).show(
                supportFragmentManager,
                "DialogFragment"
            )
        } else if (filledData[4] != filledData[5]) {
            S37DialogFragment(resources.getString(R.string.password_unmatched_error_message)).show(
                supportFragmentManager,
                "DialogFragment"
            )
        } else {
            S37DialogFragment().show(supportFragmentManager, "DialogFragment")
        }
    }

    override fun onSignUpSuccess() {
        Preference.putIsSignUp(this, true)
        Preference.putEmailId(this, filledData[2])
        Preference.putPassword(this, filledData[4])
        goBackToSignIn()
    }
}