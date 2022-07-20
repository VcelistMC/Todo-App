package com.android.todoagain.viewmodels

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.todoagain.models.ActionType
import com.android.todoagain.models.BundleKeysVals
import com.android.todoagain.database.TodoDatabaseDao
import com.android.todoagain.models.TodoItem
import kotlinx.coroutines.launch

class TodoViewModel(val database: TodoDatabaseDao, app: Application): AndroidViewModel(app) {
    val allItems = database.getAll()
    fun getItem(pos: Int): TodoItem?{
        return allItems.value?.get(pos)
    }

    fun getSize(): Int {
        return allItems.value?.size ?: 0;
    }

    fun add(title: String, subtitle: String, due_date: String){
        viewModelScope.launch {
            val newItem = TodoItem()
            newItem.title = title;
            newItem.subTitle = subtitle
            newItem.dueDate = due_date

            database.insert(newItem)
        }
    }

    fun edit(index: Int, title: String, subtitle: String, dueDate: String) {
        val todoItem = this.getItem(index)

        viewModelScope.launch {
            if(todoItem != null){
                todoItem.title = title
                todoItem.subTitle = subtitle
                todoItem.dueDate = dueDate
                database.update(todoItem)

            }
        }
    }

    fun changeStatus(index: Int, newStatus: Boolean){
        val todoItem = this.getItem(index)
        viewModelScope.launch {
            if(todoItem != null){
                todoItem.done = newStatus
                database.update(todoItem)
            }
        }
    }

    companion object {
        fun createBundle(actionType: ActionType, allowEdit: Boolean, itemIndex: Int = -1): Bundle{
            val args = Bundle()
            var title = "No Title"

            title = when(actionType){
                ActionType.ADD -> BundleKeysVals.ADD_SCREEN_TITLE
                ActionType.EDIT -> BundleKeysVals.EDIT_SCREEN_TITLE
                ActionType.VIEW -> BundleKeysVals.VIEW_SCREEN_TITLE
            }

            args.putString(BundleKeysVals.BUNDLE_TITLE, title)
            args.putBoolean(BundleKeysVals.BUNDLE_ALLOW_EDIT, allowEdit)
            args.putInt(BundleKeysVals.BUNDLE_INDEX, itemIndex)
            args.putSerializable(BundleKeysVals.ACTION, actionType)

            return args;
        }

        fun screenTitleFromBundle(bundle: Bundle?): String {
            var title: String = "No Title"
            if(bundle != null)
                title = bundle.getString(BundleKeysVals.BUNDLE_TITLE, "No Title")
//            return ((bundle != null)? bundle.getString("title"): "")

            return title
        }

        fun actionTypeFromBundle(bundle: Bundle?): ActionType {
            val action = bundle?.getSerializable(BundleKeysVals.ACTION)?: ActionType.VIEW
            return action as ActionType
        }

        fun itemIndexFromBundle(bundle: Bundle?): Int {
            return bundle?.getInt(BundleKeysVals.BUNDLE_INDEX, -1) ?: -1
        }

        fun allowEditFromBundle(bundle: Bundle?): Boolean {
            var allowEdit = false
            if(bundle != null)
                allowEdit = bundle.getBoolean(BundleKeysVals.BUNDLE_ALLOW_EDIT, false)
            return allowEdit
        }
    }
}