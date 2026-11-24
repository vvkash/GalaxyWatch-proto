package com.uncc.galaxyheart.hr.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.uncc.galaxyheart.core.ui.CurvedBottomButton
import com.uncc.galaxyheart.hr.HeartRateUiState

@Composable
fun HeartRateScreen(
    uiState: HeartRateUiState,
    onToggleRecording: () -> Unit,
) {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TimeText()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 18.dp, end = 18.dp, top = 26.dp, bottom = 54.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "❤️ Heart Rate",
                    style = MaterialTheme.typography.title3,
                    color = MaterialTheme.colors.primary,
                )

                val bpmText = when {
                    !uiState.supported -> "N/A"
                    uiState.bpm == null -> "…"
                    else -> uiState.bpm.toInt().toString()
                }
                Text(text = bpmText, fontSize = 52.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "BPM",
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.onSurfaceVariant,
                )

                Text(
                    text = when {
                        uiState.hrvRmssd != null -> "HRV (RMSSD): ${uiState.hrvRmssd.toInt()} ms"
                        uiState.hasIbi -> "HRV: …"
                        else -> "HRV: needs raw PPG"
                    },
                    style = MaterialTheme.typography.caption2,
                    color = MaterialTheme.colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "src: ${uiState.sourceName}",
                    style = MaterialTheme.typography.caption2,
                    color = MaterialTheme.colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                if (uiState.recording) {
                    Text(
                        text = "● REC  ${uiState.sampleCount} samples",
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            CurvedBottomButton(
                text = if (uiState.recording) "■ Stop" else "● Record",
                onClick = onToggleRecording,
                containerColor = if (uiState.recording) MaterialTheme.colors.error
                else MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
fun HeartRateScreenPreview() {
    HeartRateScreen(
        uiState = HeartRateUiState(
            sourceName = "Dummy (synthetic)",
            bpm = 72.0,
            hrvRmssd = 38.0,
            hasIbi = true,
            recording = true,
            sampleCount = 42,
        ),
        onToggleRecording = {},
    )
}
