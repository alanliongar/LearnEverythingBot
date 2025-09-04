package com.example.learneverythingbot.presentation.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learneverythingbot.domain.model.SubTopics

// components/AssistantText.kt - Crie um componente separado
@Composable
fun AssistantText(text: String, subTopics: List<SubTopics> = emptyList()) {
    Column {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 20.sp,
            modifier = Modifier.padding(bottom = if (subTopics.isNotEmpty()) 8.dp else 0.dp)
        )

        if (subTopics.isNotEmpty()) {
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            Text(
                "Explore os subtópicos:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            subTopics.forEach { subTopic ->
                SubTopicButton(
                    subTopic = subTopic,
                    onClick = {
                        // A navegação será tratada pelo ViewModel
                    }
                )
            }
        }
    }
}