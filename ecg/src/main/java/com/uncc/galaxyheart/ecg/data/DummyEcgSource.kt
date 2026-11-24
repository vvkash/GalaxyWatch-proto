package com.uncc.galaxyheart.ecg.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.exp
import kotlin.random.Random

/**
 * Synthetic ECG: a repeating PQRST complex sampled at ~50 Hz, with mild
 * beat-to-beat variation. Good enough to demo a live waveform and to exercise
 * the recording/CSV pipeline. No permission needed; runs on the emulator.
 */
class DummyEcgSource : EcgSource {

    override val sourceName = "Dummy (synthetic ECG)"
    override val supported = true

    override fun samples(): Flow<EcgSample> = flow {
        val rnd = Random(System.currentTimeMillis())
        val sampleIntervalMs = 20L            // 50 Hz
        var phase = 0.0                        // 0..1 within one heartbeat
        var beatPeriodSec = 1.0               // ~60 bpm
        while (true) {
            val mv = pqrst(phase) + rnd.nextDouble(-0.02, 0.02) // tiny noise
            emit(EcgSample(System.currentTimeMillis(), mv))

            phase += (sampleIntervalMs / 1000.0) / beatPeriodSec
            if (phase >= 1.0) {
                phase -= 1.0
                beatPeriodSec = (0.85..1.1).random(rnd) // HR wobble between beats
            }
            delay(sampleIntervalMs)
        }
    }

    /** Sum of Gaussians approximating P, Q, R, S, T waves over phase 0..1 (mV). */
    private fun pqrst(t: Double): Double {
        fun g(center: Double, amp: Double, width: Double) =
            amp * exp(-((t - center) * (t - center)) / (2 * width * width))
        return g(0.20, 0.10, 0.025) +   // P
            g(0.38, -0.15, 0.012) +     // Q
            g(0.40, 1.00, 0.009) +      // R
            g(0.42, -0.25, 0.012) +     // S
            g(0.62, 0.30, 0.040)        // T
    }

    private fun ClosedFloatingPointRange<Double>.random(rnd: Random) =
        start + rnd.nextDouble() * (endInclusive - start)
}
