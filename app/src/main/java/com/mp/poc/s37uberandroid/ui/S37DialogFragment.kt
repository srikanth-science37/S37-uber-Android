package com.mp.poc.s37uberandroid.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mp.poc.s37uberandroid.R
import kotlinx.android.synthetic.main.s37_dialog_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

class S37DialogFragment(var errorMessage: String = "") : DialogFragment() {

    private lateinit var listener: S37DialogListener

    interface S37DialogListener {
        fun onSignUpSuccess()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = requireActivity() as S37DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (requireContext().toString() +
                        " must implement S37DialogListener")
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.s37_dialog_fragment, container, false)

        view.btDismiss.setOnClickListener {
            dismiss()
            if (errorMessage.isEmpty()) {
                listener.onSignUpSuccess()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.setCancelable(false)

        errorMessage.apply {
            when (this.isNotEmpty()) {
                true -> {
                    view.successPulseImage.visibility = View.INVISIBLE
                    view.tvSubtitle.visibility = View.INVISIBLE
                    view.tvTitle.text = this
                    view.btDismiss.text = "Ok"
                }

                false -> {
                    val gifDrawable =
                        ((view.successPulseImage as GifImageView).drawable as GifDrawable)
                    val animationDuration = gifDrawable.duration
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(animationDuration.toLong())
                        gifDrawable.stop()
                    }
                }
            }
        }
    }
}