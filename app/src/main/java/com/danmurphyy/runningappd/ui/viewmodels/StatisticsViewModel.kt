package com.danmurphyy.runningappd.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.danmurphyy.runningappd.repository.MainRepository
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
    val mainRepository: MainRepository,
) : ViewModel() {
}