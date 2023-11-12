package com.example.daily.ui.todo

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.daily.databinding.EachTodoItemBinding


class ToDoAdapter (private val list:MutableList<ToDoData>):
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    private var listener:ToDoAdapterClicksInterface? = null
    private lateinit var lotteTic : LottieAnimationView

    fun setListner(listener:ToDoAdapterClicksInterface){
        this.listener = listener
    }
    /////// +++++++++++++++ >>>>>>>>////////////
    inner class ToDoViewHolder(val binding:EachTodoItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.textView.text = this.task
                /*binding.todoDelete.setOnClickListener {
                    listener?.onDelete(this)
                }*/
                binding.animTic.setOnClickListener {
                    lotteTic = binding.animTic
                    lotteTic.speed = (1).toFloat()
                    lotteTic.playAnimation()
                    Handler().postDelayed({
                        listener?.onDelete(this)
                        lotteTic.speed = (-10000000000).toFloat()
                        //lotteTic.playAnimation()
                    }, 1500)
                }
                binding.todoEdit.setOnClickListener {
                    listener?.onEdit(this)
                }
            }
        }
    }

    interface ToDoAdapterClicksInterface{
        fun onDelete(toDoData : ToDoData)
        fun onEdit(toDoData : ToDoData)
    }
}