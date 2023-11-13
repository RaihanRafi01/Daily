package com.example.daily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.daily.databinding.ActivityNewMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NewMessageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Select User"
        /*val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())



       binding.recylerNewMsg.adapter = adapter*/
        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("Daily").child("/UserInfo")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    Log.d("New msg",it.toString())
                    val user = it.getValue(UserModel::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                }
                binding.recylerNewMsg.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
class UserItem(val user : UserModel) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
       viewHolder.itemView.findViewById<TextView>(R.id.txtUserName).text = user.name
        var img = viewHolder.itemView.findViewById<ImageView>(R.id.imgUserPic)
        Picasso.get().load(user.imgUrl).into(img)
    }
    override fun getLayout(): Int {
       return R.layout.user_row_new_msg
    }

}
class UserModel (var email : String , var imgUrl : String ,var name : String , var number : String , var password : String , var uid : String ){
    constructor() : this("","","","","","")

}
