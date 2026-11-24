package com.uncc.galaxyheart.ecg.data

/** One ECG voltage sample. */
data class EcgSample(
    val timestampMs: Long,
    val millivolts: Double,
)
