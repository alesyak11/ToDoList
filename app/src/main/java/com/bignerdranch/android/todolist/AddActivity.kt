package com.bignerdranch.android.todolist

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class AddActivity : AppCompatActivity() {

    private lateinit var descriptionEditText: EditText
    private lateinit var priorityRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        descriptionEditText = findViewById(R.id.descriptionEditText)
        priorityRadioGroup = findViewById(R.id.priorityRadioGroup)

        val addButton: Button = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            addTask()
        }
    }

    private fun addTask() {
        val description = descriptionEditText.text.toString()
        val priority = when (priorityRadioGroup.checkedRadioButtonId) {
            R.id.highRadioButton -> 1
            R.id.mediumRadioButton -> 2
            R.id.lowRadioButton -> 3
            else -> 1
        }

        if (description.trim().isNotEmpty()) {
            val task = Task(description = description, priority = priority)
            GlobalScope.launch(Dispatchers.IO) {
                App.database.taskDao().insert(task)

                val intent = Intent("task_added")
                LocalBroadcastManager.getInstance(this@AddActivity).sendBroadcast(intent)
        }

            finish()
    }

        class AddTaskAsync(activity: AddActivity) : AsyncTask<Task, Void, Void>() {

            // Используем слабую ссылку на активити
            private val activityReference: WeakReference<AddActivity> = WeakReference(activity)

            override fun doInBackground(vararg params: Task): Void? {
                App.database.taskDao().insert(params[0])
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                // Получаем активити из слабой ссылки
                val activity = activityReference.get()

                // Проверяем, что активити не было уничтожено
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                }
            }

        }
    }
}