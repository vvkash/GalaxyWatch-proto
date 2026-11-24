package com.uncc.galaxyheart.ecg.data

import kotlinx.coroutines.flow.Flow

/** Where ECG data comes from. Swap implementations via [DataSourceConfig]. */
interface EcgSource {
    val sourceName: String
    val supported: Boolean
    fun samples(): Flow<EcgSample>
}
