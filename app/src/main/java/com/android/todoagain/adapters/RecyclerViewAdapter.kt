package com.android.todoagain.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.todoagain.models.ActionType
import com.android.todoagain.models.BundleKeysVals
import com.android.todoagain.fragments.DetailsScreen
import com.android.todoagain.R
import com.android.todoagain.models.TodoItem
import com.android.todoagain.viewmodels.TodoViewModel

class RecyclerViewAdapter() :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private  var allItems: List<TodoItem> = emptyList()


    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        lateinit var titleView: TextView
        lateinit var dueDateView: TextView
        lateinit var checkBox: CheckBox
        lateinit var todoItem: TodoItem
        var itemIndex: Int = -1
        var listenersInited = false;


        fun setItem(item: TodoItem?, index: Int){
            if(item == null) return

            titleView = view.findViewById(R.id.title_view)
            dueDateView = view.findViewById(R.id.due_date)
            checkBox = view.findViewById(R.id.checkbox)
            initItemClickListener(view)
//            initCheckBoxListener(adapterPosition)

            setTitle(item.title)
            setDueDate(item.dueDate)
            setCheckBox(item.done)
            todoItem = item
            itemIndex = index

            if(!listenersInited) {
                initCheckBoxListener(itemIndex)
                listenersInited = true
            }
        }

        private fun setTitle(title: String){titleView.text = title}

        private fun setDueDate(date: String){dueDateView.text = date}

        private fun setCheckBox(isDone: Boolean){checkBox.isChecked = isDone}

        private fun initItemClickListener(view: View){
            view.setOnClickListener{
                val fragArgs = TodoViewModel.createBundle(ActionType.VIEW, BundleKeysVals.READ_ONLY, itemIndex)
                val fragment = DetailsScreen()
                fragment.arguments = fragArgs

                val activity: AppCompatActivity = view.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_screen, fragment)
                    .addToBackStack("View")
                    .commit()
            }
        }

        fun initCheckBoxListener(index: Int){
            TODO("Changing it to onCheckedChangedListener results in an infinite loop")
            checkBox.setOnClickListener{
                val checkBox = it as CheckBox
                onCheckedChanged(this.view, checkBox.isChecked, adapterPosition)
//                checkBox.isChecked = !checkBox.isChecked
            }
        }

        private fun onCheckedChanged(view: View, isChecked: Boolean, itemIndex: Int) {
            val activity = view.context as AppCompatActivity
            val viewModel = ViewModelProvider(activity).get(TodoViewModel::class.java)
            viewModel.changeStatus(itemIndex, isChecked)

            Log.v("CheckBox", this.itemIndex.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_todo_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(allItems[position], position)
    }

    override fun getItemCount(): Int {
        return allItems.size;
    }

    fun setList(items: List<TodoItem>){
        allItems = items
        notifyDataSetChanged()
    }
}