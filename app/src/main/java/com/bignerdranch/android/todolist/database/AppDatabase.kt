package com.bignerdranch.android.todolist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.todolist.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}