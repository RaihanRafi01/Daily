package com.example.daily.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.daily.LoginActivity
import com.example.daily.R
import com.example.daily.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MessageActivity : AppCompatActivity() {
    companion object {
        var currentUser : UserModel? = null
    }
    private lateinit var binding : ActivityMessageBinding
    private val UID = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Messenger"
        binding.recylerLatestMsg.adapter = adapter
        listenForLatestMsg()
        fetchCurrentUser()
    }
val latestMsgMap = HashMap<String,ChatLogActivity.ChatMessage>()
    private fun listenForLatestMsg() {
        val ref = FirebaseDatabase.getInstance().getReference("Daily/LatestMessages/$UID")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMsg = snapshot.getValue(ChatLogActivity.ChatMessage::class.java) ?:return
                latestMsgMap[snapshot.key!!] = chatMsg
                refresh()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMsg = snapshot.getValue(ChatLogActivity.ChatMessage::class.java) ?:return
                latestMsgMap[snapshot.key!!] = chatMsg
                refresh()
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

    private fun refresh() {
        adapter.clear()
        latestMsgMap.values.forEach {
            adapter.add(LatestMsgRow(it))
        }
    }

    class LatestMsgRow(val chatMsg :ChatLogActivity.ChatMessage) : Item<GroupieViewHolder>(){
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.findViewById<ImageView>(R.id.imgChatProfileFrom)
            viewHolder.itemView.findViewById<TextView>(R.id.txtLatestmsg).text = chatMsg.text
        }

        override fun getLayout(): Int {
            return R.layout.latest_msg_row
        }

    }
    val adapter = GroupAdapter<GroupieViewHolder>()
    /*private fun dummy() {

        adapter.add(LatestMsgRow())
        adapter.add(LatestMsgRow())
        adapter.add(LatestMsgRow())
        adapter.add(LatestMsgRow())

    }*/

    private fun fetchCurrentUser() {
        val ref = FirebaseDatabase.getInstance().getReference("Daily/UserInfo/$UID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               currentUser = snapshot.getValue(UserModel::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.msg_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                val iLogin = Intent(this@MessageActivity, LoginActivity::class.java)
                getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("loginFlag", false).apply()
                startActivity(iLogin)
            }
            R.id.action_new -> {
                val iNewMsg = Intent(this@MessageActivity, NewMessageActivity::class.java)
                startActivity(iNewMsg)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}