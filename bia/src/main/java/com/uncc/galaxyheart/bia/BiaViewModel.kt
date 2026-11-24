package com.uncc.galaxyheart.bia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uncc.galaxyheart.core.CsvLogger
import com.uncc.galaxyheart.bia.data.BiaReading
import com.uncc.galaxyheart.bia.data.BiaSource
import com.uncc.galaxyheart.bia.data.DataSourceConfig
import com.uncc.galaxyheart.bia.data.DummyBiaSource
import com.uncc.galaxyheart.bia.data.PrivilegedBiaSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BiaViewModel(app: Application) : AndroidViewModel(app) {

    private val source: BiaSource =
        if (DataSourceConfig.USE_DUMMY) DummyBiaSource() else PrivilegedBiaSource()

    private val logger = CsvLogger(
        app,
        filePrefix = "bia",
        header = "timestamp_ms,body_fat_pct,skeletal_muscle_kg,body_water_pct",
    )
    private var loggerStarted = false

    private val _uiState = MutableStateFlow(
        BiaUiState(sourceName = source.sourceName, supported = source.supported)
    )
    val uiState = _uiState.asStateFlow()

    fun measure() {
        if (_uiState.value.measuring) return
        _uiState.value = _uiState.value.copy(measuring = true)
        viewModelScope.launch {
            val reading = source.measure()
            if (reading != null) {
                if (!loggerStarted) {
                    logger.start()
                    loggerStarted = true
                }
                logger.write(
                    "${reading.timestampMs},${reading.bodyFatPct}," +
                        "${reading.skeletalMuscleKg},${reading.bodyWaterPct}"
                )
                _uiState.value = _uiState.value.copy(
                    measuring = false,
                    reading = reading,
                    savedCount = _uiState.value.savedCount + 1,
                    fileName = logger.currentFile?.name,
                )
            } else {
                _uiState.value = _uiState.value.copy(measuring = false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.stop()
    }
}

data class BiaUiState(
    val sourceName: String = "",
    val supported: Boolean = true,
    val measuring: Boolean = false,
    val reading: BiaReading? = null,
    val savedCount: Int = 0,
    val fileName: String? = null,
)
