package com.uncc.galaxyheart.ecg.data

/**
 * ┌──────────────────────────────────────────────────────────────────────────┐
 * │  ECG ACCESS NOTE                                                          │
 * │  Raw ECG is NOT available through Health Services or the public SDK.      │
 * │  It requires the Samsung *Privileged* Health SDK + a signed partnership   │
 * │  with Samsung, and it cannot run on the emulator at all.                  │
 * │                                                                            │
 * │  Until that access is granted, USE_DUMMY stays true (synthetic waveform). │
 * │  Once you have the Privileged SDK wired into PrivilegedEcgSource, flip    │
 * │  USE_DUMMY = false on the real watch.                                     │
 * └──────────────────────────────────────────────────────────────────────────┘
 */
object DataSourceConfig {
    const val USE_DUMMY = true
}
