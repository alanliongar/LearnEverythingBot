package com.example.learneverythingbot.presentation.screen.ui.theme

import androidx.compose.ui.graphics.Color

object BrandColors {
    // Primárias/secundárias (exemplos — mantenha as que você já tem)
    val Primary = Color(0xFF10B981)
    val PrimaryDark = Color(0xFF0CA678)
    val PrimaryLight = Color(0xFF38C999)

    val Secondary = Color(0xFF8B5CF6)
    val SecondaryDark = Color(0xFF7C3AED)
    val SecondaryLight = Color(0xFF9B76F7)

    // Background / Surface
    val BackgroundLight = Color(0xFFFFFFFF)
    val BackgroundDark = Color(0xFF0F172A)
    val SurfaceLight = Color(0xFFFFFFFF)
    val SurfaceDark = Color(0xFF1E293B)

    // Texto / Bordas
    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF374151)
    val TextOnPrimary = Color(0xFFFFFFFF)
    val TextOnSecondary = Color(0xFFFFFFFF)
    val BorderLight = Color(0xFFE5E7EB)
    val BorderDark = Color(0xFF334155)

    // Estados
    val Error = Color(0xFFEF4444)
    val Success = Color(0xFF10B981) // ← vai virar tertiary
    val Warning = Color(0xFFF59E0B) // ← vai virar surfaceVariant

    // Bubbles (se ainda usar)
    val ChatUserBubble = Color(0xFF10B981)
    val ChatUserText = Color(0xFFFFFFFF)
    val ChatMentorBubble = Color(0xFFF3F4F6)
    val ChatMentorText = Color(0xFF111827)
}
