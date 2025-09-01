package com.example.learneverythingbot.domain.model

import android.os.Parcelable
import com.example.learneverythingbot.data.local.room.ChatHistoryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatHistory(
    val id: Int = 0,
    val userMessage: String,
    val aiResponse: String,
    val timestamp: Long = System.currentTimeMillis()
): Parcelable