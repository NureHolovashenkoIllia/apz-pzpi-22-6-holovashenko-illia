package ua.nure.holovashenko.flameguard_mobile.presentation.ui.diagrams

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun BarChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No data available for chart", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    val values = data.map { it.second.toFloat() }
    val labels = data.map { it.first }
    val maxValue = values.maxOrNull() ?: 1f
    val textMeasurer = rememberTextMeasurer()

    val barTextStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.White)
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val yAxisLabels = listOf(0, (maxValue * 0.25).roundToInt(), (maxValue * 0.5).roundToInt(), (maxValue * 0.75).roundToInt(), maxValue.roundToInt())
            Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxHeight()) {
                yAxisLabels.reversed().forEach { value ->
                    Text(text = value.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                val chartWidth = size.width
                val chartHeight = size.height
                val barWidth = chartWidth / (values.size * 1.5f)
                val spaceBetweenBars = barWidth / 2

                values.forEachIndexed { index, value ->
                    val barHeight = (value / maxValue) * chartHeight
                    val x = (index * (barWidth + spaceBetweenBars)) + spaceBetweenBars / 2
                    val y = chartHeight - barHeight

                    val barColor = when (index % 5) {
                        0 -> Color(0xFF5DADE2)
                        1 -> Color(0xFF82B96B)
                        2 -> Color(0xFF888888)
                        3 -> Color(0xFFF7C751)
                        4 -> Color(0xFFF16E5A)
                        else -> Color.Gray
                    }

                    drawRect(
                        color = barColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight)
                    )

                    val textLayoutResult = textMeasurer.measure(
                        text = value.roundToInt().toString(),
                        style = barTextStyle
                    )
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x = x + (barWidth - textLayoutResult.size.width) / 2,
                            y = y - textLayoutResult.size.height - 4.dp.toPx()
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun PreviewBarChart() {
    val sampleData = listOf(
        "05-24" to 11,
        "05-25" to 15,
        "05-26" to 9,
        "05-27" to 14,
        "05-28" to 21,
        "05-29" to 12,
        "05-30" to 18,
        "05-31" to 10,
        "06-01" to 16,
        "06-02" to 20
    )
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            BarChart(data = sampleData)
        }
    }
}