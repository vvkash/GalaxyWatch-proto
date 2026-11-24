package com.uncc.galaxyheart.bia.data

/** One body-composition measurement from the BIA sensor. */
data class BiaReading(
    val timestampMs: Long,
    val bodyFatPct: Double,
    val skeletalMuscleKg: Double,
    val bodyWaterPct: Double,
)
