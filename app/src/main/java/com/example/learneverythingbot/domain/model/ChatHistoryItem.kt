package com.example.learneverythingbot.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatHistoryItem(
    val userMessage: String,
    val aiResponse: String,
    val timestamp: Long = System.currentTimeMillis()
): Parcelable
