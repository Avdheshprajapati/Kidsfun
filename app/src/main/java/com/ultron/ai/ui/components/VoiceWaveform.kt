package com.ultron.ai.ui.components

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.BlendMode
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Arrangement
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VoiceWaveform(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    color: Color = MaterialTheme.colorScheme.primary,
    barCount: Int = 20,
    maxHeight: Float = 40f
) {
    val (phase, setPhase) = remember { mutableStateOf(0f) }
    val levels = remember { mutableStateOf(FloatArray(barCount) { 0.1f }) }

    // Animate waveform when active
    if (isActive) {
        androidx.compose.runtime.LaunchedEffect(Unit) {
            while (true) {
                setPhase(phase + 0.1f)
                // Update levels with smooth random values
                levels.value = levels.value.map { current ->
                    val target = (Math.random() * 0.8 + 0.1).toFloat()
                    current + (target - current) * 0.3f
                }.toTypedArray()
                delay(50)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = this.size.width
            val height = this.size.height
            val centerY = height / 2
            val barWidth = (width / barCount) * 0.6f
            val spacing = width / barCount

            for (i in 0 until barCount) {
                val level = if (isActive) levels.value[i] else 0.1f
                val barHeight = maxHeight * level
                val x = spacing * i + (spacing - barWidth) / 2

                // Gradient bar
                val brush = androidx.compose.ui.graphics.ShaderBrush(
                    androidx.compose.ui.graphics.LinearGradient(
                        start = androidx.compose.ui.geometry.Offset(x, centerY),
                        end = androidx.compose.ui.geometry.Offset(x, centerY - barHeight),
                        colors = listOf(
                            color.copy(alpha = 0.9f),
                            color.copy(alpha = 0.4f),
                            color.copy(alpha = 0.1f)
                        )
                    )
                )

                drawRect(
                    brush = brush,
                    topLeft = androidx.compose.ui.geometry.Offset(x, centerY - barHeight),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight * 2),
                    style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
                )

                // Glow effect
                if (isActive && level > 0.5f) {
                    drawRect(
                        color = color.copy(alpha = 0.3f * level),
                        topLeft = androidx.compose.ui.geometry.Offset(x - 2f, centerY - barHeight - 2f),
                        size = androidx.compose.ui.geometry.Size(barWidth + 4f, barHeight * 2 + 4f),
                        style = androidx.compose.ui.graphics.drawscope.DrawStyle.Stroke,
                        strokeWidth = 1f
                    )
                }
            }
        }
    }
}

@Composable
fun CircularWaveform(
    modifier: Modifier = Modifier.size(200.dp),
    isActive: Boolean,
    color: Color = MaterialTheme.colorScheme.primary,
    barCount: Int = 64
) {
    val (rotation, setRotation) = remember { mutableStateOf(0f) }
    val levels = remember { mutableStateOf(FloatArray(barCount) { 0.1f }) }

    if (isActive) {
        androidx.compose.runtime.LaunchedEffect(Unit) {
            while (true) {
                setRotation((rotation + 2f) % 360f)
                levels.value = levels.value.map { current ->
                    val target = (Math.random() * 0.9 + 0.1).toFloat()
                    current + (target - current) * 0.2f
                }.toTypedArray()
                delay(30)
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val size = min(this.size.width, this.size.height)
        val center = androidx.compose.ui.geometry.Offset(size / 2f, size / 2f)
        val innerRadius = size * 0.35f
        val maxBarLength = size * 0.12f
        val barWidth = 2.5f

        for (i in 0 until barCount) {
            val angle = (360f / barCount) * i + rotation
            val radians = Math.toRadians(angle.toDouble()).toFloat()
            val level = if (isActive) levels.value[i] else 0.1f
            val barLength = maxBarLength * level

            val startX = center.x + innerRadius * Math.cos(radians).toFloat()
            val startY = center.y + innerRadius * Math.sin(radians).toFloat()
            val endX = center.x + (innerRadius + barLength) * Math.cos(radians).toFloat()
            val endY = center.y + (innerRadius + barLength) * Math.sin(radians).toFloat()

            val alpha = 0.3f + 0.7f * level

            drawRect(
                color = color.copy(alpha = alpha),
                topLeft = androidx.compose.ui.geometry.Offset(
                    startX - barWidth / 2,
                    startY - barWidth / 2
                ),
                size = androidx.compose.ui.geometry.Size(barWidth, barLength),
                style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
            )

            // Cap glow
            if (level > 0.6f) {
                drawRect(
                    color = color.copy(alpha = 0.5f),
                    topLeft = androidx.compose.ui.geometry.Offset(endX - 3f, endY - 3f),
                    size = androidx.compose.ui.geometry.Size(6f, 6f),
                    style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
                )
            }
        }
    }
}