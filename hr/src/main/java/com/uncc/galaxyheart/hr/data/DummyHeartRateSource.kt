package com.uncc.galaxyheart.hr.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

/** Synthetic resting HR + jittered inter-beat intervals. No permission needed. */
class DummyHeartRateSource : HeartRateSource {

    override val sourceName = "Dummy (synthetic)"
    override val supported = true

    override fun samples(): Flow<HeartSample> = flow {
        val rnd = Random(System.currentTimeMillis())
        var bpm = 72.0
        while (true) {
            bpm = (bpm + rnd.nextDouble(-3.0, 3.0)).coerceIn(55.0, 120.0)
            val ibi = 60_000.0 / bpm + rnd.nextDouble(-30.0, 30.0)
            emit(HeartSample(System.currentTimeMillis(), bpm, ibi))
            delay(1_000)
        }
    }
}
