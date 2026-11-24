package com.uncc.galaxyheart.hr

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uncc.galaxyheart.core.CsvLogger
import com.uncc.galaxyheart.hr.data.DataSourceConfig
import com.uncc.galaxyheart.hr.data.DummyHeartRateSource
import com.uncc.galaxyheart.hr.data.HealthServicesHeartRateSource
import com.uncc.galaxyheart.hr.data.HeartRateSource
import com.uncc.galaxyheart.hr.data.Hrv
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeartRateViewModel(app: Application) : AndroidViewModel(app) {

    private val source: HeartRateSource =
        if (DataSourceConfig.USE_DUMMY) DummyHeartRateSource()
        else HealthServicesHeartRateSource(app)

    private val logger = CsvLogger(app, filePrefix = "hr", header = "timestamp_ms,bpm,ibi_ms")

    private val ibiWindow = ArrayDeque<Double>()
    private val ibiWindowMax = 60

    private val _uiState = MutableStateFlow(HeartRateUiState(sourceName = source.sourceName))
    val uiState = _uiState.asStateFlow()

    private var collectJob: Job? = null

    fun start() {
        if (collectJob != null) return
        collectJob = viewModelScope.launch {
            source.samples().collect { sample ->
                var rmssd = _uiState.value.hrvRmssd
                if (sample.ibiMs != null) {
                    ibiWindow.addLast(sample.ibiMs)
                    while (ibiWindow.size > ibiWindowMax) ibiWindow.removeFirst()
                    rmssd = Hrv.rmssd(ibiWindow.toList())
                }

                if (_uiState.value.recording) {
                    logger.write("${sample.timestampMs},${sample.bpm},${sample.ibiMs ?: ""}")
                }

                _uiState.value = _uiState.value.copy(
                    supported = source.supported,
                    bpm = sample.bpm,
                    hrvRmssd = rmssd,
                    hasIbi = sample.ibiMs != null,
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

data class HeartRateUiState(
    val sourceName: String = "",
    val supported: Boolean = true,
    val bpm: Double? = null,
    val hrvRmssd: Double? = null,
    val hasIbi: Boolean = false,
    val recording: Boolean = false,
    val sampleCount: Int = 0,
    val fileName: String? = null,
)
