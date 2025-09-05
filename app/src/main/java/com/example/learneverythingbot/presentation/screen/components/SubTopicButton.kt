package com.example.learneverythingbot.presentation.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.domain.model.SubTopics


@Composable
fun SubTopicButton(
    subTopic: SubTopics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val padding = when (subTopic.level) {
        1 -> 16.dp
        2 -> 32.dp
        3 -> 48.dp
        else -> 0.dp
    }

    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = padding, top = 4.dp, bottom = 4.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "• ${subTopic.title}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}