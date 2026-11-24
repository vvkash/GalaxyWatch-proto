package com.uncc.galaxyheart.ecg.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Placeholder for the REAL ECG source.
 *
 * Wiring this up requires the **Samsung Privileged Health SDK** (partnership/NDA).
 * The SDK exposes an ECG tracker that streams raw voltage samples; map each one
 * to [EcgSample] inside [samples] and set [supported] from its capability check.
 * Until then this reports unsupported and emits nothing.
 */
class PrivilegedEcgSource : EcgSource {
    override val sourceName = "Galaxy Watch ECG (Privileged SDK — not wired)"
    override val supported = false
    override fun samples(): Flow<EcgSample> = emptyFlow()
}
