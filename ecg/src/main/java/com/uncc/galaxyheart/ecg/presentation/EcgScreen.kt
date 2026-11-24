package com.uncc.galaxyheart.ecg.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.uncc.galaxyheart.core.ui.CurvedBottomButton
import com.uncc.galaxyheart.ecg.EcgUiState
import java.util.Locale

private val TraceGreen = Color(0xFF00E676)

@Composable
fun EcgScreen(
    uiState: EcgUiState,
    onToggleRecording: () -> Unit,
) {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TimeText()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp, end = 18.dp, top = 26.dp, bottom = 54.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "🫀 ECG",
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

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(vertical = 4.dp),
                ) {
                    val data = uiState.waveform
                    if (data.size < 2) return@Canvas
                    val yMin = -0.4f
                    val yMax = 1.2f
                    val dx = size.width / (data.size - 1)
                    val path = Path()
                    data.forEachIndexed { i, v ->
                        val norm = ((v - yMin) / (yMax - yMin)).coerceIn(0f, 1f)
                        val x = i * dx
                        val y = size.height - norm * size.height
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    drawPath(path, color = TraceGreen, style = Stroke(width = 2.dp.toPx()))
                }

                Text(
                    text = String.format(Locale.US, "%.2f mV", uiState.latestMv),
                    style = MaterialTheme.typography.caption1,
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
fun EcgScreenPreview() {
    EcgScreen(
        uiState = EcgUiState(
            sourceName = "Dummy (synthetic ECG)",
            supported = true,
            waveform = List(80) { (kotlin.math.sin(it / 4.0).toFloat()) * 0.5f + 0.3f },
            latestMv = 0.42,
        ),
        onToggleRecording = {},
    )
}
