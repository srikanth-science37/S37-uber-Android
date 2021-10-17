package com.mp.poc.s37uberandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.viewmodel.NurseJourneyInfoViewModel
import kotlinx.android.synthetic.main.pager_appointment_info.*

class AppointmentInfoFragment : Fragment() {
    private val viewModel: NurseJourneyInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.pager_appointment_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.journeyInfo.observe(viewLifecycleOwner, {
            tvAppointmentTime.text = it.timeEpoch
        })
    }
}