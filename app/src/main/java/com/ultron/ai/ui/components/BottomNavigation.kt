package com.ultron.ai.ui.components

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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Arrangement
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun UltronBottomNavigation(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    aiOrbState: AIOrbState = AIOrbState.Idle,
    onOrbTap: () -> Unit,
    onOrbLongPress: () -> Unit
) {
    val navItems = listOf(
        NavItem("home", "Home", R.string.nav_home),
        NavItem("memory", "Memory", R.string.nav_memory),
        NavItem("voice", "Voice", R.string.nav_voice), // Center - AI Orb
        NavItem("tools", "Tools", R.string.nav_tools),
        NavItem("profile", "Profile", R.string.nav_profile)
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Bottom nav bar
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            elevation = 16.dp.toPx(),
            tint = 0.08f,
            borderAlpha = 0.2f
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEachIndexed { index, item ->
                    if (item.route == "voice") {
                        // Center spacer for AI Orb
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(72.dp))
                    } else {
                        NavItemButton(
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .height(56.dp),
                            selected = currentRoute == item.route,
                            icon = getIconForRoute(item.route),
                            label = stringResource(item.labelRes),
                            onClick = { onNavigate(item.route) }
                        )
                    }
                }
            }
        }

        // Floating AI Orb - centered over bottom nav
        AIOrb(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.BottomCenter)
                .offset(y = -36.dp),
            size = 72f,
            state = aiOrbState,
            onTap = onOrbTap,
            onLongPress = onOrbLongPress
        )
    }
}

@Composable
private fun NavItemButton(
    modifier: Modifier,
    selected: Boolean,
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val pressScale by androidx.compose.animation.animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = androidx.compose.animation.core.spring(400, 30)
    )

    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { setPressed(true) },
                    onRelease = { setPressed(false); onClick() },
                    onCancel = { setPressed(false) }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.size(24.dp)) { icon() }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = color.copy(alpha = if (selected) 1f else 0.7f),
                fontSize = 11.sp,
                fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.W600 else androidx.compose.ui.text.font.FontWeight.W400
            )
        }
    }
}

private fun getIconForRoute(route: String): @Composable () -> Unit = when (route) {
    "home" -> { HomeIcon() }
    "memory" -> { MemoryIcon() }
    "tools" -> { ToolsIcon() }
    "profile" -> { ProfileIcon() }
    else -> { HomeIcon() }
}

@Composable
private fun HomeIcon() {
    Icon(
        imageVector = androidx.compose.material.icons.filled.Home(),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun MemoryIcon() {
    Icon(
        imageVector = androidx.compose.material.icons.filled.Psychology(),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ToolsIcon() {
    Icon(
        imageVector = androidx.compose.material.icons.filled.Build(),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ProfileIcon() {
    Icon(
        imageVector = androidx.compose.material.icons.filled.Person(),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

data class NavItem(
    val route: String,
    val label: String,
    val labelRes: Int
)