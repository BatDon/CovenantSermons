package com.batdon.covenantsermons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MasterFragmentViewModel() : ViewModel() {

    private val _showAppBar = MutableLiveData<Boolean>()
//    private val _showAppBar:MutableLiveData<Boolean> = true
    val showAppBar: LiveData<Boolean> = _showAppBar

    fun toShowAppBar(appBar: Boolean){
        _showAppBar.value=appBar
    }
}