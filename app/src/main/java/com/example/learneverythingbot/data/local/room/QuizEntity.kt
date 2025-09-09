package com.example.learneverythingbot.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "quiz_entity",
    foreignKeys = [
        ForeignKey(
            entity = TopicSummaryEntity::class,
            parentColumns = ["chat_history_user_message", "subtopic_title"],
            childColumns = ["parent_user_message", "sub_topic"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("parent_user_message"),
        Index("sub_topic"),
        Index(value = ["parent_user_message", "sub_topic"]) // índice combinado para melhorar queries
    ]
)
data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "parent_user_message") val topic: String,
    @ColumnInfo(name = "sub_topic") val subTopic: String,
    @ColumnInfo(name = "stem") val stem: String,
    @ColumnInfo(name = "question_level") val level: Int,
    @ColumnInfo(name = "right_answer") val rightAnswer: String,
    @ColumnInfo(name = "is_right") val isRight: Boolean? = null
)

