package com.adnlv.lynd.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adnlv.lynd.ui.theme.LyndTheme
import com.adnlv.lynd.util.HapticFeedbackHelper
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun <T> SmoothAreaChart(
    items: List<T>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    valueSelector: (T) -> Float,
    xLabelSelector: (T) -> String,
    tooltipTitleSelector: (T) -> String,
    tooltipValueSelector: (T) -> String,
    currencySymbol: String,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 190.dp,
    yAxisSteps: Int = 4,
) {
    if (items.isEmpty()) return

    val view = LocalView.current
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    val primaryColor = LyndTheme.colors.chartPrimary
    val textPrimaryColor = LyndTheme.colors.textPrimary
    val textSecondaryColor = LyndTheme.colors.textSecondary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderSubtleColor = LyndTheme.colors.borderSubtle

    val yAxisTextStyle =
        remember(textSecondaryColor) {
            TextStyle(
                color = textSecondaryColor,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Normal,
            )
        }

    val xAxisTextStyle =
        remember(textSecondaryColor) {
            TextStyle(
                color = textSecondaryColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
        }

    val maxValue =
        remember(items) {
            val max = items.maxOfOrNull { valueSelector(it) } ?: 0f
            if (max > 0f) max else 1f
        }

    var tooltipSize by remember { mutableStateOf(IntSize.Zero) }

    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .height(chartHeight),
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        val yAxisLabelMarginPx = with(density) { 52.dp.toPx() }
        val chartRightMarginPx = with(density) { 12.dp.toPx() }
        val chartTopPaddingPx = with(density) { 26.dp.toPx() }
        val chartBottomPaddingPx = with(density) { 32.dp.toPx() }

        val chartLeftX = yAxisLabelMarginPx
        val chartRightX = widthPx - chartRightMarginPx
        val topY = chartTopPaddingPx
        val baselineY = heightPx - chartBottomPaddingPx

        val points =
            remember(items, widthPx, heightPx, maxValue) {
                val availableWidth = (chartRightX - chartLeftX).coerceAtLeast(1f)
                val availableHeight = (baselineY - topY).coerceAtLeast(1f)
                val stepX =
                    if (items.size > 1) {
                        availableWidth / (items.size - 1)
                    } else {
                        availableWidth / 2f
                    }

                items.mapIndexed { index, item ->
                    val x =
                        if (items.size > 1) {
                            chartLeftX + index * stepX
                        } else {
                            chartLeftX + availableWidth / 2f
                        }
                    val itemVal = valueSelector(item).coerceAtLeast(0f)
                    val y = baselineY - (itemVal / maxValue) * availableHeight
                    Offset(x, y)
                }
            }

        val splinePaths =
            remember(points, baselineY) {
                if (points.isEmpty()) {
                    Pair(Path(), Path())
                } else if (points.size == 1) {
                    val linePath =
                        Path().apply {
                            moveTo(chartLeftX, points[0].y)
                            lineTo(chartRightX, points[0].y)
                        }
                    val areaPath =
                        Path().apply {
                            moveTo(chartLeftX, points[0].y)
                            lineTo(chartRightX, points[0].y)
                            lineTo(chartRightX, baselineY)
                            lineTo(chartLeftX, baselineY)
                            close()
                        }
                    Pair(linePath, areaPath)
                } else {
                    val linePath =
                        Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 0 until points.size - 1) {
                                val p0 = points[maxOf(i - 1, 0)]
                                val p1 = points[i]
                                val p2 = points[i + 1]
                                val p3 = points[minOf(i + 2, points.size - 1)]

                                val cp1x = p1.x + (p2.x - p0.x) / 6f
                                val cp1y = (p1.y + (p2.y - p0.y) / 6f).coerceIn(topY, baselineY)
                                val cp2x = p2.x - (p3.x - p1.x) / 6f
                                val cp2y = (p2.y - (p3.y - p1.y) / 6f).coerceIn(topY, baselineY)

                                cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
                            }
                        }

                    val areaPath =
                        Path().apply {
                            addPath(linePath)
                            lineTo(points.last().x, baselineY)
                            lineTo(points.first().x, baselineY)
                            close()
                        }
                    Pair(linePath, areaPath)
                }
            }

        fun selectNearest(touchX: Float) {
            if (points.isEmpty()) return
            val nearestIndex =
                points.indices.minByOrNull { index ->
                    abs(points[index].x - touchX)
                } ?: 0

            if (nearestIndex != selectedIndex) {
                onSelectedIndexChange(nearestIndex)
                HapticFeedbackHelper.vibratePageSwitch(view)
            }
        }

        Canvas(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pointerInput(items) {
                        detectTapGestures { offset ->
                            selectNearest(offset.x)
                        }
                    }.pointerInput(items) {
                        detectDragGestures { change, _ ->
                            change.consume()
                            selectNearest(change.position.x)
                        }
                    },
        ) {
            val gridDashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f), 0f)
            val indicatorDashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

            // Horizontal Dotted Grid Lines & Y-Axis Labels
            val yRange = (baselineY - topY).coerceAtLeast(1f)
            for (step in 0..yAxisSteps) {
                val fraction = step.toFloat() / yAxisSteps
                val y = topY + yRange * fraction
                val valueAtStep = maxValue * (1f - fraction)

                drawLine(
                    color = textSecondaryColor.copy(alpha = 0.2f),
                    start = Offset(chartLeftX, y),
                    end = Offset(chartRightX, y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = gridDashEffect,
                )

                val labelText = formatCompactAmount(valueAtStep, currencySymbol)
                val labelLayout =
                    textMeasurer.measure(
                        text = labelText,
                        style = yAxisTextStyle,
                    )
                drawText(
                    textLayoutResult = labelLayout,
                    topLeft =
                        Offset(
                            x = chartLeftX - labelLayout.size.width - 8.dp.toPx(),
                            y = y - labelLayout.size.height / 2f,
                        ),
                )
            }

            // Area Gradient Fill
            drawPath(
                path = splinePaths.second,
                brush =
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                primaryColor.copy(alpha = 0.22f),
                                primaryColor.copy(alpha = 0.05f),
                                Color.Transparent,
                            ),
                        startY = topY,
                        endY = baselineY,
                    ),
            )

            // Line Stroke
            drawPath(
                path = splinePaths.first,
                color = primaryColor,
                style =
                    Stroke(
                        width = 2.5.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
            )

            // Active Data Point Indicator
            if (selectedIndex in points.indices) {
                val selectedPoint = points[selectedIndex]

                // Dotted vertical line
                drawLine(
                    color = textSecondaryColor.copy(alpha = 0.45f),
                    start = Offset(selectedPoint.x, topY),
                    end = Offset(selectedPoint.x, baselineY),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = indicatorDashEffect,
                )

                // Halo circle
                drawCircle(
                    color = primaryColor.copy(alpha = 0.2f),
                    radius = 8.dp.toPx(),
                    center = selectedPoint,
                )

                // Inner dot with border
                drawCircle(
                    color = surfaceColor,
                    radius = 4.dp.toPx(),
                    center = selectedPoint,
                )
                drawCircle(
                    color = primaryColor,
                    radius = 4.dp.toPx(),
                    center = selectedPoint,
                    style = Stroke(width = 2.dp.toPx()),
                )
            }

            // X-Axis Labels
            val step = if (items.size > 8) 2 else 1
            points.forEachIndexed { index, point ->
                val shouldShow = index % step == 0

                if (shouldShow) {
                    val item = items[index]
                    val xLabel = xLabelSelector(item)
                    val labelLayout =
                        textMeasurer.measure(
                            text = xLabel,
                            style = xAxisTextStyle,
                        )
                    drawText(
                        textLayoutResult = labelLayout,
                        topLeft =
                            Offset(
                                x = point.x - labelLayout.size.width / 2f,
                                y = baselineY + 8.dp.toPx(),
                            ),
                    )
                }
            }
        }

        val tooltipMarginPx = with(density) { 8.dp.toPx() }
        val tooltipSpacingPx = with(density) { 10.dp.toPx() }
        val tooltipTopLimitPx = with(density) { 4.dp.toPx() }

        // Floating Pill Tooltip (FPT)
        if (selectedIndex in items.indices && selectedIndex in points.indices) {
            val item = items[selectedIndex]
            val selectedPoint = points[selectedIndex]

            val tooltipWidthPx = tooltipSize.width.toFloat()
            val tooltipHeightPx = tooltipSize.height.toFloat()

            val clampedX =
                if (tooltipWidthPx > 0f) {
                    (selectedPoint.x - tooltipWidthPx / 2f)
                        .coerceIn(tooltipMarginPx, widthPx - tooltipWidthPx - tooltipMarginPx)
                } else {
                    selectedPoint.x
                }

            val clampedY =
                if (tooltipHeightPx > 0f) {
                    (selectedPoint.y - tooltipHeightPx - tooltipSpacingPx)
                        .coerceAtLeast(tooltipTopLimitPx)
                } else {
                    tooltipTopLimitPx
                }

            Box(
                modifier =
                    Modifier
                        .offset { IntOffset(clampedX.roundToInt(), clampedY.roundToInt()) }
                        .onSizeChanged { tooltipSize = it },
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = surfaceColor,
                    border = BorderStroke(1.dp, borderSubtleColor.copy(alpha = 0.5f)),
                    shadowElevation = 3.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = tooltipTitleSelector(item),
                            style = MaterialTheme.typography.labelSmall,
                            color = textSecondaryColor,
                        )
                        Text(
                            text = tooltipValueSelector(item),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor,
                        )
                    }
                }
            }
        }
    }
}

private fun formatCompactAmount(
    value: Float,
    currencySymbol: String,
): String =
    when {
        value >= 1_000_000f -> {
            val millions = value / 1_000_000f
            if (millions >= 10f) {
                "$currencySymbol${millions.toInt()}M"
            } else {
                "$currencySymbol${String.format(Locale.US, "%.1f", millions)}M"
            }
        }
        value >= 1_000f -> {
            val thousands = value / 1_000f
            if (thousands >= 10f) {
                "$currencySymbol${thousands.toInt()}K"
            } else {
                "$currencySymbol${String.format(Locale.US, "%.1f", thousands)}K"
            }
        }
        else -> {
            "$currencySymbol${value.toInt()}"
        }
    }
