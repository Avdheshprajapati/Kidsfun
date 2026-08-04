package com.ultron.ai.ui.components

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.BlurStyle
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Arrangement
import androidx.compose.ui.layout.contentBoxSize
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    elevation: Float = 8.dp.toPx(),
    tint: Float = 0.06f,
    borderAlpha: Float = 0.2f,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val (hovered, setHovered) = remember { mutableStateOf(false) }
    val pressAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.12f else 0f,
        animationSpec = spring(200, 20)
    )
    val hoverAlpha by animateFloatAsState(
        targetValue = if (hovered) 0.04f else 0f,
        animationSpec = spring(200, 20)
    )

    val effectiveModifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .pointerInput(Unit) {
            if (onClick != null) {
                detectTapGestures(
                    onPress = { setPressed(true) },
                    onRelease = { setPressed(false); onClick() },
                    onCancel = { setPressed(false) }
                )
            }
            detectHover {
                setHovered(it)
            }
        }

    Box(
        modifier = effectiveModifier
            .background(
                Color.White.copy(alpha = tint + pressAlpha + hoverAlpha),
                RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = borderAlpha),
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                ambientColor = Color.Black.copy(alpha = 0.3f)
            )
    ) {
        content()
    }
}

@Composable
fun GlassCardSurface(
    modifier: Modifier = Modifier,
    elevation: Float = 4.dp.toPx(),
    tint: Float = 0.04f,
    borderAlpha: Float = 0.15f,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Color.White.copy(alpha = tint),
                shape
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = borderAlpha),
                shape = shape
            )
            .shadow(
                elevation = elevation.dp,
                shape = shape,
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                ambientColor = Color.Black.copy(alpha = 0.2f)
            )
    ) {
        content()
    }
}

@Composable
fun NeonGlowCard(
    modifier: Modifier = Modifier,
    glowColor: Color = MaterialTheme.colorScheme.primary,
    glowIntensity: Float = 0.3f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent)
    ) {
        // Outer glow layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Transparent)
                .graphicsLayer {
                    this.shadowElevation = 0f
                }
                .drawBehind {
                    val radius = 16.dp.toPx()
                    drawRect(
                        color = glowColor.copy(alpha = glowIntensity),
                        topLeft = androidx.compose.ui.geometry.Offset(-4.dp.toPx(), -4.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(
                            width = this.size.width + 8.dp.toPx(),
                            height = this.size.height + 8.dp.toPx()
                        ),
                        style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill,
                        alpha = glowIntensity
                    )
                }
        )
        GlassCard(
            modifier = Modifier.fillMaxSize(),
            elevation = 12.dp.toPx(),
            tint = 0.08f,
            borderAlpha = 0.25f,
            content = content
        )
    }
}