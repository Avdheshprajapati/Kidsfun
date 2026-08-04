package com.ultron.ai.ui.components

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.BlurStyle
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Arrangement
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun NeonButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    glowColor: Color = MaterialTheme.colorScheme.primary,
    variant: NeonButtonVariant = NeonButtonVariant.Primary,
    content: @Composable RowScope.() -> Unit
) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val (hovered, setHovered) = remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(400, 30)
    )
    val pressAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.2f else 0f,
        animationSpec = tween(100)
    )
    val hoverAlpha by animateFloatAsState(
        targetValue = if (hovered) 0.1f else 0f,
        animationSpec = tween(200)
    )
    val glowIntensity by animateFloatAsState(
        targetValue = if (hovered || pressed) 0.6f else 0.3f,
        animationSpec = spring(300, 20)
    )

    val bgColor = when (variant) {
        NeonButtonVariant.Primary -> MaterialTheme.colorScheme.primaryContainer
        NeonButtonVariant.Secondary -> MaterialTheme.colorScheme.secondaryContainer
        NeonButtonVariant.Tertiary -> MaterialTheme.colorScheme.tertiaryContainer
        NeonButtonVariant.Outline -> Color.Transparent
        NeonButtonVariant.Ghost -> Color.Transparent
    }

    val borderColor = when (variant) {
        NeonButtonVariant.Primary -> Color.Transparent
        NeonButtonVariant.Secondary -> Color.Transparent
        NeonButtonVariant.Tertiary -> Color.Transparent
        NeonButtonVariant.Outline -> glowColor.copy(alpha = 0.5f)
        NeonButtonVariant.Ghost -> Color.Transparent
    }

    val contentColor = when (variant) {
        NeonButtonVariant.Primary -> MaterialTheme.colorScheme.onPrimaryContainer
        NeonButtonVariant.Secondary -> MaterialTheme.colorScheme.onSecondaryContainer
        NeonButtonVariant.Tertiary -> MaterialTheme.colorScheme.onTertiaryContainer
        NeonButtonVariant.Outline -> glowColor
        NeonButtonVariant.Ghost -> glowColor
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { if (enabled) setPressed(true) },
                    onRelease = { if (enabled) { setPressed(false); onClick() } },
                    onCancel = { setPressed(false) }
                )
                detectHover { setHovered(it && enabled) }
            }
            .alpha(if (enabled) 1f else 0.4f)
    ) {
        // Glow background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(glowColor.copy(alpha = glowIntensity * 0.15f))
                .border(
                    width = 1.dp,
                    color = borderColor.copy(alpha = 0.3f + hoverAlpha + pressAlpha),
                    shape = RoundedCornerShape(12.dp)
                )
                .drawBehind {
                    if (hovered || pressed) {
                        drawRect(
                            color = glowColor.copy(alpha = glowIntensity * 0.3f),
                            style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
                        )
                    }
                }
        )

        // Main button content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor.copy(alpha = if (variant == NeonButtonVariant.Outline || variant == NeonButtonVariant.Ghost) 0f else 1f))
                .border(
                    width = if (variant == NeonButtonVariant.Outline) 1.5.dp else 0.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }

        // Ripple overlay
        if (pressed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(contentColor.copy(alpha = 0.15f))
            )
        }
    }
}

enum class NeonButtonVariant {
    Primary, Secondary, Tertiary, Outline, Ghost
}

@Composable
fun NeonIconButton(
    modifier: Modifier = Modifier.size(48.dp),
    onClick: () -> Unit,
    enabled: Boolean = true,
    glowColor: Color = MaterialTheme.colorScheme.primary,
    variant: NeonButtonVariant = NeonButtonVariant.Ghost,
    content: @Composable () -> Unit
) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val (hovered, setHovered) = remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = spring(400, 30)
    )
    val glowIntensity by animateFloatAsState(
        targetValue = if (hovered || pressed) 0.5f else 0.2f,
        animationSpec = spring(300, 20)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { if (enabled) setPressed(true) },
                    onRelease = { if (enabled) { setPressed(false); onClick() } },
                    onCancel = { setPressed(false) }
                )
                detectHover { setHovered(it && enabled) }
            }
            .alpha(if (enabled) 1f else 0.4f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(glowColor.copy(alpha = glowIntensity * 0.1f))
                .border(
                    width = 1.dp,
                    color = glowColor.copy(alpha = 0.2f + (if (hovered) 0.2f else 0f)),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun NeonFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    glowColor: Color = MaterialTheme.colorScheme.primary,
    expanded: Boolean = false,
    label: String? = null,
    icon: @Composable () -> Unit
) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = spring(400, 30)
    )

    val fabWidth = if (expanded && label != null) 160.dp else 56.dp

    Box(
        modifier = modifier
            .width(fabWidth)
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { setPressed(true) },
                    onRelease = { setPressed(false); onClick() },
                    onCancel = { setPressed(false) }
                )
            }
    ) {
        // Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp))
                .background(glowColor.copy(alpha = 0.2f))
                .drawBehind {
                    drawRect(
                        color = glowColor.copy(alpha = 0.3f),
                        style = androidx.compose.ui.graphics.drawscope.DrawStyle.Fill
                    )
                }
        )

        // Main FAB
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = if (expanded && label != null) Arrangement.spacedBy(12.dp) else Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(24.dp)) { icon() }
                if (expanded && label != null) {
                    Text(
                        text = label,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.W500,
                        maxLines = 1
                    )
                }
            }
        }
    }
}