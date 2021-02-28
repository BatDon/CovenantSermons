package com.example.covenantsermons.viewmodel

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.Data

interface BaseDownloadViewModel {
    var outputWorkInfos: LiveData<List<WorkInfo>>
    fun startWork()
    fun cancelWork()
    private fun createInputDataForUri(): Data


}