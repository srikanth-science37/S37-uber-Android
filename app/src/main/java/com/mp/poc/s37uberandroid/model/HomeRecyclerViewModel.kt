package com.mp.poc.s37uberandroid.model

data class HomeRecyclerViewModel(
    val taskText: String,
    val epochText: String,
    var isEnRoute: Boolean = false,
    var isUpcoming: Boolean = false
)