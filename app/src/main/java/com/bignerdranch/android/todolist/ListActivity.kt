package com.bignerdranch.android.todolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bignerdranch.android.todolist.Task
import com.bignerdranch.android.todolist.database.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var fab: FloatingActionButton

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            LoadTasksAsync().execute()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recyclerView)
        taskAdapter = TaskAdapter()
        fab = findViewById(R.id.fab)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Наблюдайте за LiveData и обновляйте адаптер при изменениях
        App.database.taskDao().getAllTasks().observe(this, { tasks ->
            taskAdapter.setTasks(tasks)
        })

        fab.setOnClickListener {
            // Открываем AddActivity при нажатии на кнопку
            val intent = Intent(this@ListActivity, AddActivity::class.java)
            startActivity(intent)
        }
        // Обработка смахивания для удаления задачи
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskToDelete = taskAdapter.getTaskAtPosition(position)
                DeleteTaskAsync().execute(taskToDelete)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("task_added"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    // AsyncTask для загрузки задач из базы данных
    private inner class LoadTasksAsync : AsyncTask<Void, Void, List<Task>>() {
        override fun doInBackground(vararg params: Void?): List<Task> {
            return App.database.taskDao().getAllTasks().value ?: emptyList()
        }

        override fun onPostExecute(result: List<Task>) {
            super.onPostExecute(result)

            // Обновите адаптер с полученными данными
            taskAdapter.setTasks(result)
        }
    }

    // Ваш адаптер
    // TaskAdapter может быть создан по аналогии с обычным RecyclerView.Adapter

    // Обработка смахивания для удаления задачи2
    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val taskToDelete = taskAdapter.getTaskAtPosition(position)
            DeleteTaskAsync().execute(taskToDelete)
        }
    })
}

// AsyncTask для удаления задачи из базы данных
private class DeleteTaskAsync : AsyncTask<Task, Void, Void>() {
    override fun doInBackground(vararg params: Task): Void? {
        App.database.taskDao().deleteTask(params[0])
        return null
    }
}
