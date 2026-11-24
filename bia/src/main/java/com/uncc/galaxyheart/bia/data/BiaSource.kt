package com.uncc.galaxyheart.bia.data

/** On-demand body-composition source. Swap implementations via [DataSourceConfig]. */
interface BiaSource {
    val sourceName: String
    val supported: Boolean
    /** Takes a single measurement, or null if unsupported. */
    suspend fun measure(): BiaReading?
}
