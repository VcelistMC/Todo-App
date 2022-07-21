package com.android.todoagain.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.todoagain.models.ActionType
import com.android.todoagain.models.BundleKeysVals
import com.android.todoagain.R
import com.android.todoagain.adapters.RecyclerViewAdapter
import com.android.todoagain.database.TodoDatabase
import com.android.todoagain.getTodayDate
import com.android.todoagain.viewmodels.TodoViewModel
import com.android.todoagain.viewmodels.TodoViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabButton: FloatingActionButton
    private lateinit var todayDateTextView: TextView
    private lateinit var adapter: RecyclerViewAdapter
    lateinit var viewModel: TodoViewModel
    lateinit var viewModelFactory: TodoViewModelFactory
    var addButtonVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    private fun initView(view: View){
        initRecyclerView(view)
        initDateView(view)
        initFabButton(view)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initListeners(view)
    }

    private fun initListeners(view: View) {
        initFabButtonClickListener()
    }


    private fun initViewModelListener() {
        // ensure viewmodel is initialized
        viewModel.allItems.observe(requireActivity()) {
            adapter.setList(it)
        }
    }

    private fun initFabButtonClickListener() {
        fabButton.setOnClickListener {
            val fragArgs: Bundle = TodoViewModel.createBundle(
                ActionType.ADD,
                BundleKeysVals.READ_WRITE
            )
            val detailsFrag = DetailsScreen()
            detailsFrag.arguments = fragArgs

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_screen, detailsFrag)
                .addToBackStack(null)
                .commit()

            fabButton.visibility = View.GONE
        }
    }

    private fun initFabButton(view: View) {
        fabButton = view.findViewById(R.id.add_task)
    }


    private fun initDateView(view: View) {
        todayDateTextView = view.findViewById(R.id.current_date)
        todayDateTextView.text = getTodayDate()
    }

    private fun initAdapterViewModel(){
        val dataSource = TodoDatabase.getInstance(requireContext()).todoDatabaseDao
        viewModelFactory = TodoViewModelFactory(dataSource, requireActivity().application);
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(TodoViewModel::class.java)
    }

    private fun initAdapter(recyclerView: RecyclerView){
        initAdapterViewModel()
        
//         Here handle callback that should send for adapter
//         like this
// 
//         adapter = RecyclerViewAdapter(
//             onItemClickListener = {itemIndex ->
//                 navigateToTodoDetails(itemIndex)
//             }, onCheckBoxChanged = { _, b,position ->
//                 viewModel.changeStatus(position, b)
//             })

        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        initViewModelListener()
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        initAdapter(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
