package com.uncc.galaxyheart.hr.data

/**
 * One heart-rate reading.
 * @param ibiMs inter-beat interval (ms) if available — needed for real HRV.
 *   Health Services HR does not provide it; the dummy source and (later) the
 *   Samsung Health Sensor SDK's raw PPG do.
 */
data class HeartSample(
    val timestampMs: Long,
    val bpm: Double,
    val ibiMs: Double? = null,
)
