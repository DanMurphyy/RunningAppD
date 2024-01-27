package com.danmurphyy.runningappd.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.danmurphyy.runningappd.R
import com.danmurphyy.runningappd.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel: MainViewModel by viewModels()

}