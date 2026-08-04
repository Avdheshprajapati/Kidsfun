package com.ultron.ai.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ultron.ai.ui.components.AIOrb
import com.ultron.ai.ui.components.AIOrbState
import com.ultron.ai.ui.components.GlassCard
import com.ultron.ai.ui.components.NeonButton
import com.ultron.ai.ui.components.NeonButtonVariant
import com.ultron.ai.ui.components.ParticleField
import com.ultron.ai.ui.theme.UltronColorScheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    aiOrbState: AIOrbState = AIOrbState.Idle,
    onOrbTap: () -> Unit,
    onOrbLongPress: () -> Unit,
    onQuickAction: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    val greeting = remember { getGreeting() }
    val (scrollOffset, setScrollOffset) = remember { mutableStateOf(0f) }

    Box(modifier = modifier.fillMaxSize()) {
        // Background particle field
        ParticleField(
            modifier = Modifier.fillMaxSize(),
            particleCount = 20,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            speed = 0.3f
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp,
                bottom = 120.dp // Space for bottom nav
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting Section
            item { GreetingSection(greeting = greeting) }

            // AI Orb Section
            item {
                AIOrbSection(
                    state = aiOrbState,
                    onTap = onOrbTap,
                    onLongPress = onOrbLongPress
                )
            }

            // Quick Actions
            item { QuickActionsGrid(onActionClick = onQuickAction) }

            // Daily Dashboard
            item { DailyDashboard(onNavigate = onNavigate) }

            // AI Suggestions
            item { AISuggestionsSection(onNavigate = onNavigate) }

            // Recent Conversations
            item { RecentConversationsSection(onNavigate = onNavigate) }
        }
    }
}

@Composable
private fun GreetingSection(greeting: String) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp.toPx(),
        tint = 0.05f,
        borderAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = greeting,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 32.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.W300,
                        letterSpacing = -0.5.sp
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Today is ${java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM d"))}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }

                // Status indicators
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatusPill(icon = "☀", text = "22°C", color = MaterialTheme.colorScheme.tertiary)
                    StatusPill(icon = "🔋", text = "87%", color = MaterialTheme.colorScheme.primary)
                    StatusPill(icon = "📅", text = "3 events", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
private fun StatusPill(icon: String, text: String, color: Color) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(
                color.copy(alpha = 0.15f),
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 12.sp)
            Text(
                text = text,
                color = color,
                fontSize = 11.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.W500
            )
        }
    }
}

@Composable
private fun AIOrbSection(
    state: AIOrbState,
    onTap: () -> Unit,
    onLongPress: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AIOrb(
            modifier = Modifier.size(160.dp),
            size = 160f,
            state = state,
            onTap = onTap,
            onLongPress = onLongPress
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        // Prompt box
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .width(320.dp),
            elevation = 2.dp.toPx(),
            tint = 0.04f,
            borderAlpha = 0.12f,
            onClick = onTap
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.filled.Mic(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(com.ultron.ai.R.string.prompt_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

        // Status text
        Text(
            text = when (state) {
                AIOrbState.Idle -> "Tap to talk • Hold for continuous"
                AIOrbState.Listening -> "Listening…"
                AIOrbState.Thinking -> "Thinking…"
                AIOrbState.Speaking -> "Speaking…"
                AIOrbState.Error -> "Error occurred"
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun QuickActionsGrid(onActionClick: (String) -> Unit) {
    val actions = listOf(
        QuickAction("research", "Research", "🔍", MaterialTheme.colorScheme.primary),
        QuickAction("write_email", "Write Email", "✉️", MaterialTheme.colorScheme.secondary),
        QuickAction("generate_code", "Generate Code", "💻", MaterialTheme.colorScheme.tertiary),
        QuickAction("translate", "Translate", "🌐", MaterialTheme.colorScheme.primary),
        QuickAction("plan_day", "Plan Day", "📅", MaterialTheme.colorScheme.secondary),
        QuickAction("analyze_image", "Analyze Image", "🖼️", MaterialTheme.colorScheme.tertiary),
        QuickAction("create_presentation", "Create Presentation", "📊", MaterialTheme.colorScheme.primary),
        QuickAction("build_website", "Build Website", "🌍", MaterialTheme.colorScheme.secondary),
    )

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp.toPx(),
        tint = 0.05f,
        borderAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Actions",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W600
                )
                Text(
                    text = "See all",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W500
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                cells = GridCells.Fixed(4),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { action ->
                    QuickActionCard(action = action, onClick = { onActionClick(action.id) })
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(action: QuickAction, onClick: () -> Unit) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val pressScale by androidx.compose.animation.animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = androidx.compose.animation.core.spring(400, 30)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { setPressed(true) },
                    onRelease = { setPressed(false); onClick() },
                    onCancel = { setPressed(false) }
                )
            }
    ) {
        GlassCard(
            modifier = Modifier.fillMaxSize(),
            elevation = if (pressed) 2.dp.toPx() else 4.dp.toPx(),
            tint = 0.06f,
            borderAlpha = 0.2f
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = action.icon, fontSize = 28.sp)
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = action.label,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W500,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

data class QuickAction(
    val id: String,
    val label: String,
    val icon: String,
    val color: Color
)

@Composable
private fun DailyDashboard(onNavigate: (String) -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp.toPx(),
        tint = 0.05f,
        borderAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Dashboard",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W600
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardCard(
                    title = "Calendar",
                    value = "3 events",
                    icon = "📅",
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { onNavigate("calendar") }
                )
                DashboardCard(
                    title = "Tasks",
                    value = "7 pending",
                    icon = "✅",
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = { onNavigate("tasks") }
                )
                DashboardCard(
                    title = "Messages",
                    value = "12 unread",
                    icon = "💬",
                    color = MaterialTheme.colorScheme.tertiary,
                    onClick = { onNavigate("messages") }
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardCard(
                    title = "Notes",
                    value = "4 new",
                    icon = "📝",
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { onNavigate("notes") }
                )
                DashboardCard(
                    title = "Files",
                    value = "2.3 GB",
                    icon = "📁",
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = { onNavigate("files") }
                )
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val pressScale by androidx.compose.animation.animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = androidx.compose.animation.core.spring(400, 30)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { setPressed(true) },
                    onRelease = { setPressed(false); onClick() },
                    onCancel = { setPressed(false) }
                )
            }
    ) {
        GlassCard(
            modifier = Modifier.fillMaxSize(),
            elevation = 2.dp.toPx(),
            tint = 0.04f,
            borderAlpha = 0.12f
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = icon, fontSize = 24.sp)
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = value,
                    color = color,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W600
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun AISuggestionsSection(onNavigate: (String) -> Unit) {
    val suggestions = listOf(
        "Summarize today's meetings",
        "Draft response to Sarah's email",
        "Optimize the React component",
        "Plan workout for this week",
        "Research quantum computing basics"
    )

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp.toPx(),
        tint = 0.05f,
        borderAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AI Suggestions",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W600
                )
                Icon(
                    imageVector = androidx.compose.material.icons.filled.Psychology(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestions.forEach { suggestion ->
                    AISuggestionChip(text = suggestion, onClick = { onNavigate("chat") })
                }
            }
        }
    }
}

@Composable
private fun AISuggestionChip(text: String, onClick: () -> Unit) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val pressScale by androidx.compose.animation.animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = androidx.compose.animation.core.spring(400, 30)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { setPressed(true) },
                    onRelease = { setPressed(false); onClick() },
                    onCancel = { setPressed(false) }
                )
            }
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = 1.dp.toPx(),
            tint = 0.03f,
            borderAlpha = 0.1f
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.filled.ArrowForward(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun RecentConversationsSection(onNavigate: (String) -> Unit) {
    val conversations = listOf(
        Conversation("1", "Project planning session", "2 hours ago", 12, true),
        Conversation("2", "Code review for auth module", "Yesterday", 8, false),
        Conversation("3", "Travel itinerary to Tokyo", "3 days ago", 24, true),
        Conversation("4", "Learning Rust fundamentals", "1 week ago", 45, false),
    )

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp.toPx(),
        tint = 0.05f,
        borderAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Conversations",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W600
                )
                Text(
                    text = "View all",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W500
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                conversations.forEach { conv ->
                    ConversationTile(conversation = conv, onClick = { onNavigate("chat") })
                }
            }
        }
    }
}

@Composable
private fun ConversationTile(conversation: Conversation, onClick: () -> Unit) {
    val (pressed, setPressed) = remember { mutableStateOf(false) }
    val pressScale by androidx.compose.animation.animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = androidx.compose.animation.core.spring(400, 30)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { setPressed(true) },
                    onRelease = { setPressed(false); onClick() },
                    onCancel = { setPressed(false) }
                )
            }
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = 1.dp.toPx(),
            tint = 0.03f,
            borderAlpha = 0.1f
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.filled.Chat(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = conversation.title,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.W500,
                            maxLines = 1
                        )
                        if (conversation.pinned) {
                            Icon(
                                imageVector = androidx.compose.material.icons.filled.PushPin(),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${conversation.timeAgo} • ${conversation.messageCount} messages",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }

                Icon(
                    imageVector = androidx.compose.material.icons.filled.ChevronRight(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

data class Conversation(
    val id: String,
    val title: String,
    val timeAgo: String,
    val messageCount: Int,
    val pinned: Boolean
)

private fun getGreeting(): String {
    val hour = java.time.LocalTime.now().hour
    return when {
        hour < 12 -> "Good Morning"
        hour < 17 -> "Good Afternoon"
        hour < 22 -> "Good Evening"
        else -> "Good Night"
    }
}