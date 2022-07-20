package com.android.todoagain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items_table")
data class TodoItem (
    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "subtitle")
    var subTitle: String = "",

    @ColumnInfo(name = "priority")
    var priority: Int = 0,

    @ColumnInfo(name = "due_date")
    var dueDate: String = "",

    @ColumnInfo(name = "done")
    var done: Boolean = false
)
