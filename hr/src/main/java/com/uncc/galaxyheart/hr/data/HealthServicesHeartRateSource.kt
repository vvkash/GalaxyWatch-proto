package com.uncc.galaxyheart.hr.data

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DeltaDataType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.guava.await

/**
 * The REAL source: optical HR via Health Services. Inter-beat intervals are not
 * exposed here, so HRV reads as unavailable until we move to the Samsung Health
 * Sensor SDK (raw PPG) in Phase 1b.
 */
class HealthServicesHeartRateSource(context: Context) : HeartRateSource {

    private val measureClient = HealthServices.getClient(context).measureClient

    override val sourceName = "Galaxy Watch (Health Services)"

    override var supported: Boolean = true
        private set

    override fun samples(): Flow<HeartSample> = callbackFlow {
        val capabilities = measureClient.getCapabilitiesAsync().await()
        supported = DataType.HEART_RATE_BPM in capabilities.supportedDataTypesMeasure
        if (!supported) {
            close()
            return@callbackFlow
        }

        val callback = object : MeasureCallback {
            override fun onAvailabilityChanged(
                dataType: DeltaDataType<*, *>,
                availability: Availability,
            ) { /* no-op */ }

            override fun onDataReceived(data: DataPointContainer) {
                val now = System.currentTimeMillis()
                data.getData(DataType.HEART_RATE_BPM).forEach { point ->
                    trySend(HeartSample(now, point.value, ibiMs = null))
                }
            }
        }

        measureClient.registerMeasureCallback(DataType.HEART_RATE_BPM, callback)
        awaitClose {
            measureClient.unregisterMeasureCallbackAsync(DataType.HEART_RATE_BPM, callback)
        }
    }
}
