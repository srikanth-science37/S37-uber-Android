package com.mp.poc.s37uberandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mp.poc.s37uberandroid.model.NurseJourneyInfoModel

class NurseJourneyInfoViewModel : ViewModel() {
    private val mutableJourneyInfo = MutableLiveData<NurseJourneyInfoModel>()
    val journeyInfo: LiveData<NurseJourneyInfoModel> get() = mutableJourneyInfo

    fun updateCardUi(item: NurseJourneyInfoModel) {
        mutableJourneyInfo.value = item
    }
}