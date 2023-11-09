package com.example.daily.ui.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.daily.R
import com.example.daily.databinding.FragmentAddTodoPopUpBinding
import com.example.daily.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddTodoPopUpFragment : DialogFragment() {
    private lateinit var binding: FragmentAddTodoPopUpBinding
    private lateinit var listener: DialogAddTodoClickListener
    private var toDoData: ToDoData? = null

    fun setListener(listener: DialogAddTodoClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "AddTodoPopUpFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = AddTodoPopUpFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTodoPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            toDoData = ToDoData(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )
            binding.todoTxt.setText(toDoData?.task)
        }
        resiterEvents()
    }

    private fun resiterEvents() {
        binding.todosave.setOnClickListener {
            val todoTask = binding.todoTxt.text.toString()
            if (todoTask.isNotEmpty()) {
                if (toDoData == null){
                    listener.onSaveTask(todoTask, binding.todoTxt)
                }else{
                    toDoData?.task = todoTask
                    listener.onUpdateTask(toDoData!! , binding.todoTxt)
                }

            } else {
                Toast.makeText(context, "please add some Task ", Toast.LENGTH_SHORT).show()
            }
        }

        binding.todoclose.setOnClickListener {
            dismiss()
        }
    }


    interface DialogAddTodoClickListener {
        fun onSaveTask(todo: String, todotxt: EditText)
        fun onUpdateTask(toDoData: ToDoData, todotxt: EditText)
    }

}