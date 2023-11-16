package com.example.daily.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.daily.R
import com.example.daily.databinding.ActivityNewMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.parcel.Parcelize

class NewMessageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Select User"
        fetchUsers()
    }
companion object{
    val USER_KEY = "USER_KEY"
}
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("Daily").child("/UserInfo")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    //Log.d("New msg",it.toString())
                    val user = it.getValue(UserModel::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val iChat = Intent(view.context,ChatLogActivity::class.java)
                    iChat.putExtra(USER_KEY,userItem.user)
                    startActivity(iChat)
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
@Parcelize
class UserModel (var email : String , var imgUrl : String ,var name : String , var number : String , var password : String , var uid : String ) : Parcelable{
    constructor() : this("","","","","","")

}
