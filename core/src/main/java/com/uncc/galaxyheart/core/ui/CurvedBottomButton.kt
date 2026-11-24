package com.uncc.galaxyheart.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

/**
 * A flat-top, convex-bottom shape. The bottom edge bulges downward so the button
 * follows the round screen's curve — the same look as Wear M3's EdgeButton, but
 * built from a plain Compose shape so it works on the stable Material 2 stack.
 */
val BottomEdgeShape = GenericShape { size, _ ->
    val corner = size.height * 0.45f
    moveTo(0f, corner)
    quadraticTo(0f, 0f, corner, 0f)                       // rounded top-left
    lineTo(size.width - corner, 0f)                       // flat top edge
    quadraticTo(size.width, 0f, size.width, corner)       // rounded top-right
    cubicTo(                                              // convex bottom hugging the curve
        size.width, size.height,
        0f, size.height,
        0f, corner,
    )
    close()
}

/**
 * Full-width action button anchored to the bottom of a round watch screen.
 */
@Composable
fun CurvedBottomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    height: Dp = 46.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(BottomEdgeShape)
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.caption1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 9.dp, start = 24.dp, end = 24.dp),
        )
    }
}
