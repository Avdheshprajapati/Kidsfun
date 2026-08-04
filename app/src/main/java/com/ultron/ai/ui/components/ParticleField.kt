package com.ultron.ai.ui.components

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.spring
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
fun ParticleField(
    modifier: Modifier = Modifier.fillMaxSize(),
    particleCount: Int = 30,
    color: Color = MaterialTheme.colorScheme.primary,
    speed: Float = 1f
) {
    val (time, setTime) = remember { mutableStateOf(0f) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        while (true) {
            setTime((time + 0.01f * speed) % 360f)
            delay(16)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = this.size.width
        val height = this.size.height

        for (i in 0 until particleCount) {
            val seed = (i * 137.5f + time * 10f) % 360f
            val radius = (width.min(height) * 0.4f) * (0.3f + (i % 5) * 0.1f)
            val angle = Math.toRadians(seed.toDouble()).toFloat()
            val x = width / 2 + radius * Math.cos(angle).toFloat()
            val y = height / 2 + radius * Math.sin(angle).toFloat()
            val size = 1.5f + (i % 3).toFloat()
            val alpha = 0.05f + (i % 4) * 0.03f

            drawRect(
                color = color.copy(alpha = alpha),
                topLeft = androidx.compose.ui.geometry.Offset(x - size / 2, y - size / 2),
                size = androidx.compose.ui.geometry.Size(size, size),
                style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
            )

            // Connect nearby particles with subtle lines
            if (i < particleCount - 1) {
                val nextSeed = ((i + 1) * 137.5f + time * 10f) % 360f
                val nextRadius = (width.min(height) * 0.4f) * (0.3f + ((i + 1) % 5) * 0.1f)
                val nextAngle = Math.toRadians(nextSeed.toDouble()).toFloat()
                val nextX = width / 2 + nextRadius * Math.cos(nextAngle).toFloat()
                val nextY = height / 2 + nextRadius * Math.sin(nextAngle).toFloat()

                val distance = Math.hypot((nextX - x).toDouble(), (nextY - y).toDouble()).toFloat()
                if (distance < 80f) {
                    drawRect(
                        color = color.copy(alpha = 0.02f * (1f - distance / 80f)),
                        topLeft = androidx.compose.ui.geometry.Offset(x, y),
                        size = androidx.compose.ui.geometry.Size(distance, 0.5f),
                        style = androidx.compose.ui.graphics.drawscope.DrawStyle.Stroke,
                        strokeWidth = 0.5f
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingParticles(
    modifier: Modifier = Modifier.fillMaxSize(),
    color: Color = MaterialTheme.colorScheme.primary,
    density: Float = 0.0001f
) {
    val (particles, setParticles) = remember { mutableStateOf<List<Particle>>(emptyList()) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        // Initialize particles
        val initialParticles = mutableListOf<Particle>()
        val width = 400f // Will be updated in draw
        val height = 800f
        val count = (width * height * density).toInt().coerceAtMost(50)
        repeat(count) {
            initialParticles.add(Particle.random(width, height))
        }
        setParticles(initialParticles)

        // Animation loop
        while (true) {
            setParticles(particles.map { it.update() })
            delay(16)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = this.size.width
        val height = this.size.height

        particles.forEach { p ->
            drawRect(
                color = color.copy(alpha = p.alpha),
                topLeft = androidx.compose.ui.geometry.Offset(p.x - p.size / 2, p.y - p.size / 2),
                size = androidx.compose.ui.geometry.Size(p.size, p.size),
                style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
            )
        }
    }
}

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val size: Float,
    val alpha: Float,
    val life: Float
) {
    companion object {
        fun random(width: Float, height: Float): Particle {
            return Particle(
                x = (Math.random() * width).toFloat(),
                y = (Math.random() * height).toFloat(),
                vx = (Math.random() * 0.5 - 0.25).toFloat(),
                vy = (Math.random() * -0.5 - 0.1).toFloat(),
                size = (Math.random() * 3 + 1).toFloat(),
                alpha = (Math.random() * 0.3 + 0.05).toFloat(),
                life = (Math.random() * 1000 + 500).toFloat()
            )
        }
    }

    fun update(): Particle {
        val newX = x + vx
        val newY = y + vy
        val newLife = life - 1

        return if (newLife <= 0 || newY < -10) {
            Particle.random(400f, 800f).copy(y = 810f)
        } else {
            copy(x = newX, y = newY, life = newLife, alpha = alpha * 0.999f)
        }
    }
}

@Composable
fun NeonGrid(
    modifier: Modifier = Modifier.fillMaxSize(),
    color: Color = MaterialTheme.colorScheme.primary,
    gridSize: Float = 60f,
    opacity: Float = 0.03f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = this.size.width
        val height = this.size.height

        // Vertical lines
        var x = 0f
        while (x < width) {
            drawRect(
                color = color.copy(alpha = opacity),
                topLeft = androidx.compose.ui.geometry.Offset(x, 0f),
                size = androidx.compose.ui.geometry.Size(1f, height),
                style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
            )
            x += gridSize
        }

        // Horizontal lines
        var y = 0f
        while (y < height) {
            drawRect(
                color = color.copy(alpha = opacity),
                topLeft = androidx.compose.ui.geometry.Offset(0f, y),
                size = androidx.compose.ui.geometry.Size(width, 1f),
                style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
            )
            y += gridSize
        }
    }
}