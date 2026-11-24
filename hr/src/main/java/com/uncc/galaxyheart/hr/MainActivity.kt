package com.uncc.galaxyheart.hr

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uncc.galaxyheart.hr.data.DataSourceConfig
import com.uncc.galaxyheart.hr.presentation.HeartRateScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: HeartRateViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { viewModel.start() }

            LaunchedEffect(Unit) {
                if (DataSourceConfig.USE_DUMMY) viewModel.start()
                else permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
            }

            HeartRateScreen(uiState = uiState, onToggleRecording = viewModel::toggleRecording)
        }
    }
}
