package com.mp.poc.s37uberandroid.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.model.SignUpInputFieldModel
import com.mp.poc.s37uberandroid.ui.adapter.SignUpInputFieldRVAdapter
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity(),
    SignUpInputFieldRVAdapter.OnSignUpScreenUpdateListener {

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
            Toast.makeText(this, "All filled", Toast.LENGTH_SHORT).show()
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

    }
}