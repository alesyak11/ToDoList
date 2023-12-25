package com.bignerdranch.android.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val priority: Int // 1 for High, 2 for Medium, 3 for Low
)