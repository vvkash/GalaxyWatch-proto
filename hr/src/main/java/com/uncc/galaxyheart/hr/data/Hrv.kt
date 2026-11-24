package com.uncc.galaxyheart.hr.data

import kotlin.math.sqrt

/** Time-domain HRV from a window of inter-beat intervals (ms). */
object Hrv {
    /** RMSSD in ms, or null if there aren't enough intervals yet. */
    fun rmssd(ibisMs: List<Double>): Double? {
        if (ibisMs.size < 2) return null
        var sumSq = 0.0
        for (i in 1 until ibisMs.size) {
            val d = ibisMs[i] - ibisMs[i - 1]
            sumSq += d * d
        }
        return sqrt(sumSq / (ibisMs.size - 1))
    }
}
