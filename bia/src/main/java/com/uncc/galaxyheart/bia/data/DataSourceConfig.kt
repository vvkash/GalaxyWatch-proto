package com.uncc.galaxyheart.bia.data

/**
 * ┌──────────────────────────────────────────────────────────────────────────┐
 * │  BIA ACCESS NOTE                                                          │
 * │  Body-composition (multi-frequency BIA) data is NOT available through the │
 * │  public SDK. It requires the Samsung *Privileged* Health SDK + a signed   │
 * │  partnership, and cannot run on the emulator.                             │
 * │                                                                            │
 * │  Until access is granted, USE_DUMMY stays true (synthetic readings).      │
 * │  Wire the real on-demand BIA tracker into PrivilegedBiaSource, then flip  │
 * │  USE_DUMMY = false on the real watch.                                     │
 * └──────────────────────────────────────────────────────────────────────────┘
 */
object DataSourceConfig {
    const val USE_DUMMY = true
}
