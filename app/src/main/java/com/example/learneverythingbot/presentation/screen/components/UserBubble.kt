package com.example.learneverythingbot.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learneverythingbot.presentation.screen.ui.theme.Purple40

@Composable
fun UserBubble(text: String) {
    Row(Modifier.fillMaxWidth()) {
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .widthIn(max = 320.dp)
                .background(Purple40, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(text, color = Color.White, lineHeight = 20.sp)
        }
    }
}