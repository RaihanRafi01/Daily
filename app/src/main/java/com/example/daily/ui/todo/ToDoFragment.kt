package com.example.daily.ui.todo

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daily.R
import com.example.daily.databinding.FragmentAddTodoPopUpBinding
import com.example.daily.databinding.FragmentToDoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

private lateinit var binding: FragmentToDoBinding
private var popUpFragment : AddTodoPopUpFragment? = null
private val UID = FirebaseAuth.getInstance().uid.toString()
private var dbref =  Firebase.database.getReference("Daily").child("Tasks").child(UID)
private lateinit var adapterTODO : ToDoAdapter
private lateinit var todoList  :MutableList<ToDoData>
class ToDoFragment : Fragment(), AddTodoPopUpFragment.DialogAddTodoClickListener,
    ToDoAdapter.ToDoAdapterClicksInterface {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentToDoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        getDataFromFireBase()
        resiterEvents()
        val currentDate = LocalDate.now()
        val day = currentDate.dayOfMonth.toString()
        val month = currentDate.month.toString()
        val week = currentDate.dayOfWeek.toString()
        binding.txtMonth.text = day+" "+month.take(3)
        binding.txtDay.text = "  "+week
    }

    private fun getDataFromFireBase() {
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                todoList.clear()
                for (taskSnapShot in snapshot.children){
                    val todoTask = taskSnapShot.key?.let {
                        ToDoData(it,taskSnapShot.value.toString())
                    }
                    if (todoTask != null){
                        todoList.add(todoTask)
                    }
                }
                adapterTODO.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun resiterEvents() {
       binding.addtodomain.setOnClickListener {
           if(popUpFragment != null )
               childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

           popUpFragment = AddTodoPopUpFragment()
           popUpFragment!!.setListener(this)
           popUpFragment!!.show(
               childFragmentManager,
               AddTodoPopUpFragment.TAG
           )

       }
    }
    private fun initVars() {
        binding.recylerTask.setHasFixedSize(true)
        binding.recylerTask.layoutManager = LinearLayoutManager(context)
        todoList = mutableListOf()
        adapterTODO = ToDoAdapter(todoList)
        adapterTODO.setListner(this)
        binding.recylerTask.adapter = adapterTODO
    }

    override fun onSaveTask(todo: String, todotxt: EditText) {
        dbref.push().setValue(todo).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Task Added",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_LONG).show()
            }
            todotxt.text = null
            popUpFragment!!.dismiss()
        }
    }

    override fun onUpdateTask(toDoData: ToDoData, todotxt: EditText) {
        val map = HashMap<String,Any>()
        map[toDoData.taskId] = toDoData.task
        dbref.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Task Updated Successfully",Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_LONG).show()
            }
            todotxt.text = null
            popUpFragment!!.dismiss()
        }
    }

    override fun onDelete(toDoData: ToDoData) {
        dbref.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context,"Task Completed",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onEdit(toDoData: ToDoData) {
        if(popUpFragment != null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

            popUpFragment = AddTodoPopUpFragment.newInstance(toDoData.taskId,toDoData.task)
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(childFragmentManager,AddTodoPopUpFragment.TAG)
        }
    }


