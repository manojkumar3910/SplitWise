package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SpendWiseAssetDot1
import com.example.ui.theme.SpendWiseAssetDot2
import com.example.ui.theme.SpendWiseAssetDot3
import com.example.ui.theme.SpendWiseGold
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun PortfolioPerformanceChart(
    modifier: Modifier = Modifier,
    selectedTimeframe: String = "6M",
    onTimeframeSelected: (String) -> Unit = {}
) {
    val timeframes = listOf("1M", "3M", "6M", "1Y", "ALL")
    val dataPoints = listOf(
        0.80f, // Jan
        0.65f, // Feb
        0.70f, // Mar
        0.45f, // Apr
        0.28f, // May
        0.12f  // Jun
    )
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Portfolio Performance",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SpendWiseSurfaceContainerLow)
                    .border(1.dp, SpendWiseSurfaceVariant, RoundedCornerShape(12.dp))
                    .padding(2.dp)
            ) {
                timeframes.forEach { tf ->
                    val isSelected = tf == selectedTimeframe
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) SpendWisePrimaryContainer else Color.Transparent)
                            .clickable { onTimeframeSelected(tf) }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tf,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SpendWisePrimary else Color(0xFF4B5563)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height - 24.dp.toPx()
                val stepX = w / (dataPoints.size - 1)

                val points = dataPoints.mapIndexed { index, normY ->
                    Offset(x = index * stepX, y = normY * h)
                }

                // Draw gradient filled area
                val fillPath = Path().apply {
                    moveTo(0f, h)
                    lineTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        val cx = (prev.x + curr.x) / 2f
                        cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                    }
                    lineTo(w, h)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SpendWisePrimary.copy(alpha = 0.25f),
                            SpendWisePrimary.copy(alpha = 0.02f)
                        ),
                        startY = 0f,
                        endY = h
                    )
                )

                // Draw stroke path
                val strokePath = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        val cx = (prev.x + curr.x) / 2f
                        cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                    }
                }

                drawPath(
                    path = strokePath,
                    color = SpendWisePrimary,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Draw data points
                points.forEach { pt ->
                    drawCircle(
                        color = SpendWisePrimary,
                        radius = 4.5.dp.toPx(),
                        center = pt
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.5.dp.toPx(),
                        center = pt
                    )
                }
            }
        }

        // X-axis labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            months.forEach { m ->
                Text(
                    text = m,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}

@Composable
fun SimpleSparklineChart(
    modifier: Modifier = Modifier,
    height: Dp = 80.dp
) {
    Canvas(modifier = modifier.fillMaxWidth().height(height)) {
        val w = size.width
        val h = size.height

        val points = listOf(
            Offset(0f, h * 0.8f),
            Offset(w * 0.25f, h * 0.65f),
            Offset(w * 0.50f, h * 0.70f),
            Offset(w * 0.75f, h * 0.45f),
            Offset(w * 1.00f, h * 0.20f)
        )

        val fillPath = Path().apply {
            moveTo(0f, h)
            lineTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]
                val cx = (prev.x + curr.x) / 2f
                cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
            }
            lineTo(w, h)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    SpendWisePrimary.copy(alpha = 0.20f),
                    SpendWisePrimary.copy(alpha = 0.0f)
                ),
                startY = 0f,
                endY = h
            )
        )

        val strokePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]
                val cx = (prev.x + curr.x) / 2f
                cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
            }
        }

        drawPath(
            path = strokePath,
            color = SpendWisePrimary,
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        points.forEach { pt ->
            drawCircle(
                color = SpendWisePrimary,
                radius = 3.5.dp.toPx(),
                center = pt
            )
        }
    }
}

@Composable
fun AssetAllocationDonut(
    modifier: Modifier = Modifier,
    stocksPercent: Float = 48f,
    mutualFundsPercent: Float = 34f,
    goldPercent: Float = 10f,
    silverPercent: Float = 8f,
    sizeDp: Dp = 160.dp
) {
    val colors = listOf(
        SpendWisePrimary,           // Stocks (Terracotta)
        SpendWiseAssetDot2,         // Mutual Funds (Peach/Muted Rose)
        SpendWiseGold,              // Gold (Amber Gold)
        SpendWiseAssetDot1          // Silver/Other (Soft Rose)
    )

    val sweepAngles = listOf(
        (stocksPercent / 100f) * 360f,
        (mutualFundsPercent / 100f) * 360f,
        (goldPercent / 100f) * 360f,
        (silverPercent / 100f) * 360f
    )

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = 22.dp.toPx()
            val arcSize = Size(size.width - strokeWidthPx, size.height - strokeWidthPx)
            val topLeft = Offset(strokeWidthPx / 2f, strokeWidthPx / 2f)

            var startAngle = -90f
            sweepAngles.forEachIndexed { i, sweep ->
                drawArc(
                    color = colors[i],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt)
                )
                startAngle += sweep
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "4",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )
            Text(
                text = "Assets",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF53433F)
            )
        }
    }
}

@Composable
fun CircularProgressGauge(
    progressPercent: Float,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 5.dp,
    progressColor: Color = SpendWisePrimary,
    trackColor: Color = SpendWisePrimaryContainer.copy(alpha = 0.5f)
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)

            // Draw track
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Draw progress
            val sweep = (progressPercent.coerceIn(0f, 100f) / 100f) * 360f
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun AllocationLegendItem(
    label: String,
    percent: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = SpendWiseOnBackground
            )
        }
        Text(
            text = percent,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = SpendWiseOnBackground
        )
    }
}

