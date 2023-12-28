package com.bignerdranch.android.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.todolist.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY priority")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert
    fun insert(task: Task)

    @Delete
    fun deleteTask(task: Task)
}