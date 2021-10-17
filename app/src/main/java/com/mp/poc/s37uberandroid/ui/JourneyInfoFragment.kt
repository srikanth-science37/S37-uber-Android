package com.mp.poc.s37uberandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.ui.notifications.S37NotificationManager.NotificationState.*
import com.mp.poc.s37uberandroid.utils.ViewUtils.setGif
import com.mp.poc.s37uberandroid.utils.ViewUtils.setImage
import com.mp.poc.s37uberandroid.viewmodel.NurseJourneyInfoViewModel
import kotlinx.android.synthetic.main.pager_journey_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JourneyInfoFragment : Fragment() {
    private val viewModel: NurseJourneyInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.pager_journey_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.journeyInfo.observe(viewLifecycleOwner, {
            etaText.text = it.timeEpoch

            when (it.journeyStatus) {
                STARTED -> {
                    scheduleText.alpha = 0.3f
                    schedulePulseImage.visibility = View.GONE
                    scheduleCircleImage.visibility = View.VISIBLE
                    enRouteCircleImage.visibility = View.GONE
                    enRoutePulseImage.visibility = View.VISIBLE
                    setImage(requireActivity(), scheduleLineImage, R.drawable.ic_green_line)
                    setImage(requireActivity(), enRouteCircleImage, R.drawable.ic_green_circle)
                }
                ARRIVING_SHORTLY -> {
                    if (enRouteUpdatesPulseImage.isVisible) return@observe
                    enRoutePulseImage.visibility = View.GONE
                    enRouteCircleImage.visibility = View.VISIBLE
                    setImage(requireActivity(), enRouteCircleImage, R.drawable.ic_green_circle)
                    setImage(requireActivity(), enRouteLineImage, R.drawable.ic_half_green_line)
                    enRouteUpdatesCircleImage.visibility = View.GONE
                    enRouteUpdatesPulseImage.visibility = View.VISIBLE
                }
                ARRIVED -> {
                    enRouteUpdatesPulseImage.visibility = View.VISIBLE
                    enRouteUpdatesCircleImage.visibility = View.GONE
                    setGif(requireActivity(), enRouteUpdatesPulseImage, R.drawable.arrived_pulse)
                    enRouteText.alpha = 0.3f
                    setImage(requireActivity(), enRouteLineImage, R.drawable.ic_green_line)
                    setImage(
                        requireActivity(),
                        enRouteUpdatesCircleImage,
                        R.drawable.ic_green_circle
                    )
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(200)
                        launch {
                            enRouteUpdatesPulseImage.visibility = View.GONE
                            enRouteUpdatesCircleImage.visibility = View.VISIBLE
                        }
                    }
                    enRouteUpdatesText.setTextColor(resources.getColor(R.color.green, null))
                    val arrivedText = "Arrived"
                    enRouteUpdatesText.text = arrivedText
                }
            }
        })
    }
}