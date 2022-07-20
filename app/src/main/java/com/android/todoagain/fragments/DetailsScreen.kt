package com.android.todoagain.fragments

import android.app.ActionBar
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.android.todoagain.models.ActionType
import com.android.todoagain.R
import com.android.todoagain.formatDate
import com.android.todoagain.models.BundleKeysVals
import com.android.todoagain.viewmodels.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.card_todo_item.*
import kotlinx.android.synthetic.main.fragment_details_screen.*

class DetailsScreen : Fragment() {
    private lateinit var titleView: EditText
    private lateinit var subtitleView: EditText
    private lateinit var duedateView: TextView
    private lateinit var setDateButton: ImageButton
    private lateinit var saveBtn: FloatingActionButton
    private lateinit var viewModel: TodoViewModel
    private lateinit var actionType: ActionType
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("OptionsMenu", "fragment starting")
        setHasOptionsMenu(true)
//        actionBar = requireActivity().findViewById(R.id.menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.v("OptionsMenu", "menu created")
        inflater.inflate(R.menu.action_menu, menu)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, arguments)
        initClickListeners()
        initVars()
        initDataIfAvailable()
    }

    private fun initVars() {
        datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener(DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            duedateView.text = formatDate(i3, i2, i)
        })

        viewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
        actionType = TodoViewModel.actionTypeFromBundle(arguments)
    }

    private fun initClickListeners() {
        initSaveFabButton()
        initDatePickerButton()
    }

    private fun startEditFragment() {
        val itemIndex = TodoViewModel.itemIndexFromBundle(arguments)
        val args = TodoViewModel.createBundle(ActionType.EDIT, BundleKeysVals.READ_WRITE, itemIndex)
        val editFragment = DetailsScreen()
        editFragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_screen, editFragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.edit_btn -> {
                startEditFragment()
                return true
            }

            else -> {
                Log.v("OptionsMenu", "Default")

                true
            }
        }
    }

    private fun initDatePickerButton() {
        setDateButton.setOnClickListener{
            datePickerDialog.show()
        }
    }

    private fun initSaveFabButton() {
        saveBtn.setOnClickListener{
            val title = titleView.text.toString()
            val subtitle = subtitleView.text.toString()
            val dueDate = duedateView.text.toString()
            if(actionType == ActionType.ADD) {
                viewModel.add(title, subtitle, dueDate)
            }
            else {
                val index = TodoViewModel.itemIndexFromBundle(arguments)
                viewModel.edit(index, title, subtitle, dueDate)
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initView(view: View, args: Bundle?) {
        setHasOptionsMenu(true)
        val allowEdit = TodoViewModel.allowEditFromBundle(arguments)
        val title = TodoViewModel.screenTitleFromBundle(arguments)
        requireActivity().setTitle(title);

        initTitleView(view, allowEdit)

        initSubtitleView(view, allowEdit)

        initDueDateView(view, allowEdit)

        initSaveBtnView(view, allowEdit)
    }

    private fun initSaveBtnView(view: View, allowEdit: Boolean) {
        saveBtn = view.findViewById(R.id.save_btn)
        saveBtn.visibility = if (allowEdit) View.VISIBLE else View.GONE
    }

    private fun initDueDateView(view: View, allowEdit: Boolean) {
        duedateView = view.findViewById(R.id.due_date_view)

        setDateButton = view.findViewById(R.id.set_date_btn)
        setDateButton.isEnabled = allowEdit
    }

    private fun initSubtitleView(view: View, allowEdit: Boolean) {
        subtitleView = view.findViewById(R.id.subtitle_text_field)
        subtitleView.isEnabled = allowEdit
    }

    private fun initTitleView(view: View, allowEdit: Boolean) {
        titleView = view.findViewById(R.id.title_text_field)
        titleView.isEnabled = allowEdit
    }


    private fun initDataIfAvailable(){
        val index = TodoViewModel.itemIndexFromBundle(arguments)
        if(actionType == ActionType.ADD){
            setItemOrEmpty()
            return
        }

        val selectedItem = viewModel.getItem(index)
        if (selectedItem != null) {
            setItemOrEmpty(selectedItem.title, selectedItem.subTitle, selectedItem.dueDate)
        }
    }

    private fun setItemOrEmpty(title: String = "", subtitle: String = "", dueDate: String = ""){
        titleView.setText(title)
        subtitleView.setText(subtitle)
        duedateView.setText(dueDate)
    }
}