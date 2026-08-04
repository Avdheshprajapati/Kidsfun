package com.ultron.ai.ui.components

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.CombinedModifier
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SweepGradient
import androidx.compose.ui.graphics.drawscope.BlendMode
import androidx.compose.ui.graphics.drawscope.drawCircle
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AIOrb(
    modifier: Modifier = Modifier,
    size: Float = 160.dp.toPx(),
    state: AIOrbState = AIOrbState.Idle,
    onTap: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
    onDrag: ((Float, Float) -> Unit)? = null
) {
    val (dragOffsetX, setDragOffsetX) = remember { mutableStateOf(0f) }
    val (dragOffsetY, setDragOffsetY) = remember { mutableStateOf(0f) }
    val (isDragging, setIsDragging) = remember { mutableStateOf(false) }
    val (scale, setScale) = remember { mutableStateOf(1f) }

    // Breathing animation for idle state
    val breathScale by animateFloatAsState(
        targetValue = if (state == AIOrbState.Idle) 1.02f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 0, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    // Pulse animation for listening/speaking
    val pulseScale by animateFloatAsState(
        targetValue = if (state == AIOrbState.Listening || state == AIOrbState.Speaking) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 0, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Wave animation for thinking
    val wavePhase by animateFloatAsState(
        targetValue = if (state == AIOrbState.Thinking) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 0, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    // Particle field animation
    val particleOffset by animateFloatAsState(
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, delayMillis = 0, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "particleOffset"
    )

    val combinedScale = breathScale * pulseScale * scale

    val glowColor = when (state) {
        AIOrbState.Idle -> MaterialTheme.colorScheme.primary
        AIOrbState.Listening -> MaterialTheme.colorScheme.secondary
        AIOrbState.Thinking -> MaterialTheme.colorScheme.tertiary
        AIOrbState.Speaking -> MaterialTheme.colorScheme.primary
        AIOrbState.Error -> MaterialTheme.colorScheme.error
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap?.invoke() },
                    onLongPress = { onLongPress?.invoke() },
                    onPress = { setScale(0.95f) },
                    onRelease = { setScale(1f) }
                )
                detectDragGestures(
                    onDragStart = { setIsDragging(true) },
                    onDrag = { change ->
                        setDragOffsetX(dragOffsetX + change.positionChange().x)
                        setDragOffsetY(dragOffsetY + change.positionChange().y)
                        onDrag?.invoke(dragOffsetX + change.positionChange().x, dragOffsetY + change.positionChange().y)
                    },
                    onDragEnd = { setIsDragging(false) }
                )
            }
            .graphicsLayer {
                scaleX = combinedScale
                scaleY = combinedScale
                translationX = dragOffsetX
                translationY = dragOffsetY
            }
    ) {
        // Outer glow ring
        OrbGlowRing(
            size = size,
            color = glowColor,
            state = state,
            wavePhase = wavePhase,
            particleOffset = particleOffset
        )

        // Middle ring
        OrbRing(
            size = size * 0.85f,
            color = glowColor,
            state = state
        )

        // Inner core
        OrbCore(
            size = size * 0.65f,
            color = glowColor,
            state = state,
            particleOffset = particleOffset
        )

        // Center dot
        OrbCenter(
            size = size * 0.25f,
            color = glowColor,
            state = state
        )

        // State indicator
        if (state == AIOrbState.Listening) {
            OrbWaveform(size = size * 1.2f, color = glowColor)
        }
    }
}

enum class AIOrbState {
    Idle, Listening, Thinking, Speaking, Error
}

@Composable
private fun OrbGlowRing(
    size: Float,
    color: Color,
    state: AIOrbState,
    wavePhase: Float,
    particleOffset: Float
) {
    Canvas(modifier = Modifier.size(size.dp)) {
        val radius = size / 2
        val center = androidx.compose.ui.geometry.Offset(radius, radius)

        // Multiple glow layers
        for (i in 0..2) {
            val glowRadius = radius + (i * 8f)
            val alpha = when (state) {
                AIOrbState.Idle -> 0.08f - (i * 0.02f)
                AIOrbState.Listening -> 0.2f - (i * 0.04f)
                AIOrbState.Thinking -> 0.15f - (i * 0.03f)
                AIOrbState.Speaking -> 0.25f - (i * 0.05f)
                AIOrbState.Error -> 0.2f - (i * 0.04f)
            }

            drawCircle(
                color = color.copy(alpha = alpha),
                center = center,
                radius = glowRadius,
                style = androidx.compose.ui.graphics.drawscope.DrawStyle.Stroke,
                strokeWidth = 2f
            )
        }

        // Particle field
        if (state != AIOrbState.Error) {
            val particleCount = 12
            for (i in 0 until particleCount) {
                val angle = (particleOffset + (360f / particleCount) * i) * (Math.PI / 180)
                val particleRadius = radius + 20f + (i % 3 * 10f)
                val x = center.x + particleRadius * Math.cos(angle).toFloat()
                val y = center.y + particleRadius * Math.sin(angle).toFloat()
                val particleAlpha = 0.15f + (i % 4 * 0.05f)

                drawCircle(
                    color = color.copy(alpha = particleAlpha),
                    center = androidx.compose.ui.geometry.Offset(x, y),
                    radius = 2f + (i % 3).toFloat(),
                    style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
                )
            }
        }
    }
}

@Composable
private fun OrbRing(
    size: Float,
    color: Color,
    state: AIOrbState
) {
    Canvas(modifier = Modifier.size(size.dp)) {
        val radius = size / 2
        val center = androidx.compose.ui.geometry.Offset(radius, radius)
        val strokeWidth = 3f

        // Gradient ring
        val brush = ShaderBrush(
            SweepGradient(
                center = center,
                colors = listOf(
                    color.copy(alpha = 0.3f),
                    color.copy(alpha = 0.8f),
                    color.copy(alpha = 0.3f)
                ),
                startAngle = 0f,
                endAngle = 360f
            )
        )

        drawCircle(
            brush = brush,
            center = center,
            radius = radius - strokeWidth / 2,
            style = androidx.compose.ui.graphics.drawscope.DrawStyle.Stroke,
            strokeWidth = strokeWidth
        )
    }
}

@Composable
private fun OrbCore(
    size: Float,
    color: Color,
    state: AIOrbState,
    particleOffset: Float
) {
    Canvas(modifier = Modifier.size(size.dp)) {
        val radius = size / 2
        val center = androidx.compose.ui.geometry.Offset(radius, radius)

        // Core gradient
        val brush = ShaderBrush(
            androidx.compose.ui.graphics.RadialGradient(
                center = center,
                radius = radius,
                colors = listOf(
                    color.copy(alpha = 0.9f),
                    color.copy(alpha = 0.4f),
                    Color.Transparent
                )
            )
        )

        drawCircle(
            brush = brush,
            center = center,
            radius = radius,
            style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
        )

        // Inner particles
        if (state == AIOrbState.Thinking || state == AIOrbState.Listening) {
            val particleCount = 8
            for (i in 0 until particleCount) {
                val angle = (particleOffset * 0.5f + (360f / particleCount) * i) * (Math.PI / 180)
                val particleRadius = radius * 0.6f
                val x = center.x + particleRadius * Math.cos(angle).toFloat()
                val y = center.y + particleRadius * Math.sin(angle).toFloat()

                drawCircle(
                    color = color.copy(alpha = 0.4f),
                    center = androidx.compose.ui.geometry.Offset(x, y),
                    radius = 1.5f,
                    style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
                )
            }
        }
    }
}

@Composable
private fun OrbCenter(
    size: Float,
    color: Color,
    state: AIOrbState
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.9f))
            .graphicsLayer {
                this.shadowElevation = 20f
            }
    )
}

@Composable
private fun OrbWaveform(
    size: Float,
    color: Color
) {
    Canvas(modifier = Modifier.size(size.dp)) {
        val center = androidx.compose.ui.geometry.Offset(size / 2, size / 2)
        val barCount = 32
        val barWidth = 2f
        val spacing = 4f
        val maxHeight = size * 0.35f

        // Simulated audio levels (in real app, this would come from audio recorder)
        val levels = remember { mutableStateOf(FloatArray(barCount) { (Math.random() * 0.5 + 0.1).toFloat() }) }

        for (i in 0 until barCount) {
            val angle = (360f / barCount) * i * (Math.PI / 180)
            val innerRadius = size * 0.45f
            val level = levels.value[i]
            val barHeight = maxHeight * level

            val startX = center.x + innerRadius * Math.cos(angle).toFloat()
            val startY = center.y + innerRadius * Math.sin(angle).toFloat()
            val endX = center.x + (innerRadius + barHeight) * Math.cos(angle).toFloat()
            val endY = center.y + (innerRadius + barHeight) * Math.sin(angle).toFloat()

            drawRect(
                color = color.copy(alpha = 0.6f * level),
                topLeft = androidx.compose.ui.geometry.Offset(
                    startX - barWidth / 2,
                    startY - barWidth / 2
                ),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
            )
        }
    }
}