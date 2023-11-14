package com.example.daily.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.daily.R
import com.example.daily.databinding.ActivityChatLogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatLogActivity : AppCompatActivity() {
    companion object {
        val TAG = "ChatLog"
    }
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding : ActivityChatLogBinding
    private var ref = FirebaseDatabase.getInstance().getReference("Daily").child("Messages")
    var UID = FirebaseAuth.getInstance().uid
    var toUser : UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyChatLog.adapter = adapter
        toUser = intent.getParcelableExtra<UserModel>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.name

        listenForMessages()

        binding.btnSendMsg.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages() {
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null){
                    if (chatMessage.fromId == UID){
                        val currentUser = MessageActivity.currentUser
                        adapter.add(ChatFromItem(chatMessage.text,currentUser))
                    }else{
                        val toUser = intent.getParcelableExtra<UserModel>(NewMessageActivity.USER_KEY)
                        adapter.add(ChatToItem(chatMessage.text,toUser))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    class ChatMessage(
        val id: String?,
        val text: String,
        val fromId: String?,
        val toId: String?, val timeStamp: Long) {
        constructor() : this("","","","",0)
    }

    private fun performSendMessage() {

            val text = binding.edtxtMsg.text.toString()
            val toId = toUser?.uid
            ref = ref.push()
            val chatMessage = ChatMessage(ref.key,text, UID,toId,System.currentTimeMillis()/1000)
            Log.e("MSG",chatMessage.toString())
            ref.setValue(chatMessage)
    }
}
class ChatFromItem(val text : String, val user: UserModel?) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.txtChatFrom).text = text
        val targetImg = viewHolder.itemView.findViewById<ImageView>(R.id.imgChatProfileFrom)
        val uri = user?.imgUrl
        Picasso.get().load(uri).into(targetImg)
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChatToItem(val text: String, val user: UserModel?) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.txtChatTo).text = text
        val targetImg = viewHolder.itemView.findViewById<ImageView>(R.id.imgChatProfileTo)
        val uri = user?.imgUrl
        Picasso.get().load(uri).into(targetImg)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}