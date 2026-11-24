package com.uncc.galaxyheart.ecg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uncc.galaxyheart.core.CsvLogger
import com.uncc.galaxyheart.ecg.data.DataSourceConfig
import com.uncc.galaxyheart.ecg.data.DummyEcgSource
import com.uncc.galaxyheart.ecg.data.EcgSource
import com.uncc.galaxyheart.ecg.data.PrivilegedEcgSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EcgViewModel(app: Application) : AndroidViewModel(app) {

    private val source: EcgSource =
        if (DataSourceConfig.USE_DUMMY) DummyEcgSource() else PrivilegedEcgSource()

    private val logger = CsvLogger(app, filePrefix = "ecg", header = "timestamp_ms,mv")

    private val waveform = ArrayDeque<Float>()
    private val waveformMax = 150 // ~3 s at 50 Hz

    private val _uiState = MutableStateFlow(
        EcgUiState(sourceName = source.sourceName, supported = source.supported)
    )
    val uiState = _uiState.asStateFlow()

    private var collectJob: Job? = null

    fun start() {
        if (collectJob != null) return
        collectJob = viewModelScope.launch {
            source.samples().collect { sample ->
                waveform.addLast(sample.millivolts.toFloat())
                while (waveform.size > waveformMax) waveform.removeFirst()

                if (_uiState.value.recording) {
                    logger.write("${sample.timestampMs},${sample.millivolts}")
                }

                _uiState.value = _uiState.value.copy(
                    waveform = waveform.toList(),
                    latestMv = sample.millivolts,
                    sampleCount = _uiState.value.sampleCount + if (_uiState.value.recording) 1 else 0,
                )
            }
        }
    }

    fun toggleRecording() {
        val state = _uiState.value
        if (state.recording) {
            logger.stop()
            _uiState.value = state.copy(recording = false)
        } else {
            val file = logger.start()
            _uiState.value = state.copy(recording = true, sampleCount = 0, fileName = file.name)
        }
    }

    override fun onCleared() {
        super.onCleared()
        collectJob?.cancel()
        logger.stop()
    }
}

data class EcgUiState(
    val sourceName: String = "",
    val supported: Boolean = true,
    val waveform: List<Float> = emptyList(),
    val latestMv: Double = 0.0,
    val recording: Boolean = false,
    val sampleCount: Int = 0,
    val fileName: String? = null,
)
