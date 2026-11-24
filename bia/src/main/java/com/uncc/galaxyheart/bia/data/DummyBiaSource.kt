package com.uncc.galaxyheart.bia.data

import kotlinx.coroutines.delay
import kotlin.random.Random

/** Synthetic body-composition reading after a short simulated measurement. */
class DummyBiaSource : BiaSource {

    override val sourceName = "Dummy (synthetic BIA)"
    override val supported = true

    private val rnd = Random(System.currentTimeMillis())

    override suspend fun measure(): BiaReading {
        delay(1_800) // BIA takes a few seconds on a real watch
        return BiaReading(
            timestampMs = System.currentTimeMillis(),
            bodyFatPct = rnd.nextDouble(12.0, 28.0),
            skeletalMuscleKg = rnd.nextDouble(28.0, 40.0),
            bodyWaterPct = rnd.nextDouble(50.0, 65.0),
        )
    }
}
