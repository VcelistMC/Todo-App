package com.android.todoagain.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.todoagain.models.TodoItem

@Dao
interface TodoDatabaseDao {
    @Insert
    suspend fun insert(item: TodoItem)

    @Update
    suspend fun update(item: TodoItem)

    @Query("SELECT * FROM todo_items_table ORDER BY itemId DESC")
    fun getAll(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todo_items_table WHERE done = 1 ORDER BY itemId DESC")
    fun getCompleted(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todo_items_table WHERE done = 0 ORDER BY itemId DESC")
    fun getUncompleted(): LiveData<List<TodoItem>>

    @Delete
    suspend fun delete(item: TodoItem)
}