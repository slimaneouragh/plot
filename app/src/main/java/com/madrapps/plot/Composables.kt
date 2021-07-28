package com.madrapps.plot

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@Composable
fun GraphColumn(
    modifier: Modifier,
    yStart: Dp,
    scale: Float,
    color: Color = MaterialTheme.colors.onSurface,
    values: () -> List<Value> = { listOf(Value("0", 0f)) },
    content: @Composable () -> Unit = {
        values().forEach { (text, _) ->
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
                color = color
            )
        }
    }
) {
    val valueList = values()
    val steps = valueList.size - 1
    val min = valueList.first().value
    val max = valueList.last().value
    val stepSize = if (steps != 0) {
        (max - min) / steps
    } else 0f

    Layout(content, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            val availableHeight = (constraints.maxHeight - yStart.toPx())
            var yPos = availableHeight.toInt()

            placeables.forEach { placeable ->
                yPos -= (placeable.height / 2f).toInt() + 1
                placeable.place(x = 0, y = yPos)
                yPos -= (stepSize * availableHeight / max * scale).toInt() - (placeable.height / 2f).toInt()
            }
        }
    }
}

data class Value(val text: String, val value: Float)