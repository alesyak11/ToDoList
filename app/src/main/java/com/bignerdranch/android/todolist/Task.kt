package com.bignerdranch.android.todolist

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val priority: Int // 1 for High, 2 for Medium, 3 for Low
){
    fun getPriorityColor(context: Context): Int {
        return when (priority) {
            1 -> ContextCompat.getColor(context, R.color.colorPriorityHigh)
            2 -> ContextCompat.getColor(context, R.color.colorPriorityMedium)
            3 -> ContextCompat.getColor(context, R.color.colorPriorityLow)
            else -> ContextCompat.getColor(context, R.color.black) // Черный цвет по умолчанию
        }
    }
}