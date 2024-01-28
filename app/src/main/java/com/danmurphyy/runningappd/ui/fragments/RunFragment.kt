package com.danmurphyy.runningappd.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.danmurphyy.runningappd.R
import com.danmurphyy.runningappd.adapters.RunAdapter
import com.danmurphyy.runningappd.databinding.FragmentRunBinding
import com.danmurphyy.runningappd.ui.viewmodels.MainViewModel
import com.danmurphyy.runningappd.utils.SortType
import com.danmurphyy.runningappd.utils.TrackingUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {

    private lateinit var binding: FragmentRunBinding
    private val viewModel: MainViewModel by viewModels()
    private val mRunAdapter: RunAdapter by lazy { RunAdapter() }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.all { it.value }
            if (granted) {
                // All permissions are granted, handle accordingly
            } else {
                // Handle if permissions are not granted
                val shouldShowRationale =
                    permissions.any { !it.value && shouldShowRequestPermissionRationale(it.key) }
                if (shouldShowRationale) {
                    showLocationPermissionDialog()
                } else {
                    showSettingsRedirectDialog()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRunBinding.bind(view)
        setupRecyclerView()

        sortSelection()

        viewModel.runs.observe(viewLifecycleOwner) {
            mRunAdapter.submitList(it)
        }

        if (!TrackingUtils.hasLocationPermissions(requireContext())) {
            requestLocationPermissions()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment2_to_trackingFragment)
        }
    }

    private fun sortSelection() {
        when (viewModel.sortType) {
            SortType.DATE -> binding.spFilter.setSelection(0)
            SortType.DISTANCE -> binding.spFilter.setSelection(1)
            SortType.CALORIES_BURNED -> binding.spFilter.setSelection(2)
            SortType.RUNNING_TIME -> binding.spFilter.setSelection(3)
            SortType.AVG_SPEED -> binding.spFilter.setSelection(4)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view1: View?,
                pos: Int,
                id: Long,
            ) {
                when (pos) {
                    0 -> viewModel.sortRuns(SortType.DATE)
                    1 -> viewModel.sortRuns(SortType.DISTANCE)
                    2 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
                    3 -> viewModel.sortRuns(SortType.RUNNING_TIME)
                    4 -> viewModel.sortRuns(SortType.AVG_SPEED)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
    }

    private fun setupRecyclerView() = binding.rvRuns.apply {
        adapter = mRunAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestLocationPermissions() {
        requestLocationPermissionLauncher.launch(
            TrackingUtils.PERMISSIONS
        )
    }

    private fun showLocationPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.location_permission))
            .setMessage(getString(R.string.location_permission_is_required))
            .setPositiveButton(getString(R.string.grant)) { _, _ ->
                requestLocationPermissions()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun showSettingsRedirectDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.location_permission)
            .setMessage(getString(R.string.location_in_app_settings))
            .setPositiveButton(R.string.settings) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

}
