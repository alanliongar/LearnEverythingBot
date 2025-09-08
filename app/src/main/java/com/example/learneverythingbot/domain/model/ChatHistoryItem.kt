package com.example.learneverythingbot.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatHistoryItem(
    val userMessage: String,
    val aiResponse: String,
    val timestamp: Long = System.currentTimeMillis()
): Parcelable
