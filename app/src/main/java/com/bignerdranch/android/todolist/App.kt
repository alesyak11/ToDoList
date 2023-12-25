package com.bignerdranch.android.todolist

import android.app.Application
import androidx.room.Room
import com.bignerdranch.android.todolist.database.AppDatabase

class App : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "task-database")
            .build()
    }
}