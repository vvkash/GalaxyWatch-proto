package com.uncc.galaxyheart.hr.data

import kotlinx.coroutines.flow.Flow

/** Where heart data comes from. Swap implementations via [DataSourceConfig]. */
interface HeartRateSource {
    val sourceName: String
    val supported: Boolean
    fun samples(): Flow<HeartSample>
}
