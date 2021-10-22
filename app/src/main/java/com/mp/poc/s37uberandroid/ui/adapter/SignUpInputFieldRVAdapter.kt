package com.mp.poc.s37uberandroid.ui.adapter

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.model.SignUpInputFieldModel

class SignUpInputFieldRVAdapter(
    private val mList: List<SignUpInputFieldModel>,
    private val onUpdateListener: OnSignUpScreenUpdateListener
) :
    RecyclerView.Adapter<SignUpInputFieldRVAdapter.ViewHolder>() {

    private val fieldsValuesList = arrayOf("", "", "", "", "", "", "", "")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.registration_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]

        holder.itemTitle.text = itemViewModel.fieldTitle

        holder.editTextField.addTextChangedListener {
            it?.apply {
                fieldsValuesList[position] = this.toString().trim()
            }

            if (allFieldsFilled()) {
                onUpdateListener.onInputFieldsUpdate(true, fieldsValuesList)
            } else {
                onUpdateListener.onInputFieldsUpdate(false, fieldsValuesList)
            }
        }

        if (position == 4 || position == 5) {
            holder.passwordRequiredText.visibility = if (position == 4) View.VISIBLE else View.GONE
            holder.passwordVisibilityButton.isEnabled = true
            holder.passwordVisibilityButton.isClickable = true
            holder.editTextField.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_universal_view,
                0
            )
            holder.editTextField.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD + InputType.TYPE_CLASS_TEXT

            holder.passwordVisibilityButton.setOnClickListener {
                passwordAction(holder.editTextField)
            }
        } else {
            holder.passwordRequiredText.visibility = View.GONE
            holder.passwordVisibilityButton.isEnabled = false
            holder.passwordVisibilityButton.isClickable = false
            holder.editTextField.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            if (position == 7) {
                holder.editTextField.imeOptions = EditorInfo.IME_ACTION_DONE
            } else {
                holder.editTextField.imeOptions = EditorInfo.IME_ACTION_NEXT
            }
            if (position == 3) {
                holder.editTextField.inputType = InputType.TYPE_CLASS_PHONE
                holder.editTextField.setEms(10)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun getItems(): List<SignUpInputFieldModel> = mList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: AppCompatTextView = itemView.findViewById(R.id.tvItemTitle)
        val passwordRequiredText: AppCompatTextView =
            itemView.findViewById(R.id.tvPasswordRequirement)
        val editTextField: TextInputEditText = itemView.findViewById(R.id.etInputField)
        val passwordVisibilityButton: AppCompatButton = itemView.findViewById(R.id.btPasswordText)
    }

    private fun passwordAction(editTextField: TextInputEditText) {
        val isPasswordEnabled = isPasswordTextEnabled(editTextField)

        if (isPasswordEnabled) {
            editTextField.inputType = InputType.TYPE_CLASS_TEXT
            editTextField.setSelection(if (editTextField.text?.isNotBlank() == true) editTextField.text?.length!! else 0)
            editTextField.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_universal_view_disabled,
                0
            )
        } else {
            //TODO Need to check why value 129 works for password char, even though there is no such constant with this value.
            editTextField.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD + InputType.TYPE_CLASS_TEXT
            editTextField.setSelection(if (editTextField.text?.isNotBlank() == true) editTextField.text?.length!! else 0)
            editTextField.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_universal_view,
                0
            )
        }
    }

    private fun isPasswordTextEnabled(inputField: TextInputEditText): Boolean {
        return inputField.inputType != InputType.TYPE_CLASS_TEXT
    }

    interface OnSignUpScreenUpdateListener {
        fun onInputFieldsUpdate(shouldEnableSignUpButton: Boolean, allData: Array<String>)
    }

    private fun allFieldsFilled(): Boolean {
        var areFieldsFilled = false

        for (data in fieldsValuesList) {
            if (data.trim().isEmpty()) {
                areFieldsFilled = false
                break
            } else {
                areFieldsFilled = true
            }
        }

        return areFieldsFilled
    }
}