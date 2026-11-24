package com.uncc.galaxyheart.bia.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.uncc.galaxyheart.bia.BiaUiState
import com.uncc.galaxyheart.bia.data.BiaReading
import com.uncc.galaxyheart.core.ui.CurvedBottomButton
import java.util.Locale

@Composable
fun BiaScreen(
    uiState: BiaUiState,
    onMeasure: () -> Unit,
) {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TimeText()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp, end = 18.dp, top = 26.dp, bottom = 54.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "⚖️ Body Comp",
                    style = MaterialTheme.typography.title3,
                    color = MaterialTheme.colors.primary,
                )

                if (!uiState.supported) {
                    Text(
                        text = "Requires Samsung\nPrivileged SDK",
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                    )
                }

                when {
                    uiState.measuring -> Text(
                        text = "Measuring…",
                        style = MaterialTheme.typography.body1,
                    )
                    uiState.reading != null -> Metrics(uiState.reading)
                    else -> Text(
                        text = "No reading yet",
                        style = MaterialTheme.typography.caption1,
                        color = MaterialTheme.colors.onSurfaceVariant,
                    )
                }

                Text(
                    text = "src: ${uiState.sourceName}",
                    style = MaterialTheme.typography.caption2,
                    color = MaterialTheme.colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                if (uiState.savedCount > 0) {
                    Text(
                        text = "${uiState.savedCount} saved",
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.onSurfaceVariant,
                    )
                }
            }

            CurvedBottomButton(
                text = if (uiState.measuring) "…" else "Measure",
                onClick = onMeasure,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun Metrics(reading: BiaReading) {
    fun fmt(v: Double) = String.format(Locale.US, "%.1f", v)
    Text("Body fat: ${fmt(reading.bodyFatPct)} %", style = MaterialTheme.typography.caption1)
    Text("Muscle: ${fmt(reading.skeletalMuscleKg)} kg", style = MaterialTheme.typography.caption1)
    Text("Water: ${fmt(reading.bodyWaterPct)} %", style = MaterialTheme.typography.caption1)
}

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
fun BiaScreenPreview() {
    BiaScreen(
        uiState = BiaUiState(
            sourceName = "Dummy (synthetic BIA)",
            supported = true,
            reading = BiaReading(0L, 18.4, 34.2, 57.8),
            savedCount = 3,
        ),
        onMeasure = {},
    )
}
