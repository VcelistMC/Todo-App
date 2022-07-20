package com.android.todoagain.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.todoagain.database.TodoDatabase
import com.android.todoagain.database.TodoDatabaseDao

class TodoViewModelFactory(
    private val database: TodoDatabaseDao,
    private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}