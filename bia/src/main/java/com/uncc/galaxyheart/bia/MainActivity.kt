package com.uncc.galaxyheart.bia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uncc.galaxyheart.bia.presentation.BiaScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: BiaViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BiaScreen(uiState = uiState, onMeasure = viewModel::measure)
        }
    }
}
