package com.example.daily.messages

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.daily.LoginActivity
import com.example.daily.R
import com.example.daily.databinding.ActivityMessageBinding
import com.example.daily.messages.NewMessageActivity.Companion.USER_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.j2objc.annotations.Weak
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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
        binding.recylerLatestMsg.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val row = item as LatestMsgRow
            val iChat = Intent(this,ChatLogActivity::class.java)
            iChat.putExtra(USER_KEY,row.chatPartnerUser)
            startActivity(iChat)
        }
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
        var chatPartnerUser: UserModel? = null
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            val chatPartnerId :String
            if (chatMsg.fromId == FirebaseAuth.getInstance().uid){
                chatPartnerId = chatMsg.toId.toString()
            }else{
                chatPartnerId = chatMsg.fromId.toString()
            }
            val ref = FirebaseDatabase.getInstance().getReference("Daily/UserInfo/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatPartnerUser = snapshot.getValue(UserModel::class.java)
                    viewHolder.itemView.findViewById<TextView>(R.id.txtLatestName).text = chatPartnerUser?.name
                    Picasso.get().load(chatPartnerUser?.imgUrl).into(viewHolder.itemView.findViewById<ImageView>(R.id.imgLatestUser))
                    //viewHolder.itemView.findViewById<TextView>(R.id.txtTimeStamp).text = chatPartnerUser?.email.toString()
                    //Log.e("TIME",chatPartnerUser?.toString().toString())
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            val date = Date(chatMsg.timeStamp)
            var time = ""
            //val afterWeak = Date(System.currentTimeMillis()+604800000)
           //val afterDay = Date(System.currentTimeMillis()+86400000)
            if (Date(System.currentTimeMillis()-86400000)>date){
                val simpleDateFormat = SimpleDateFormat("E")
                time = simpleDateFormat.format(date)
            }
            else if (Date(System.currentTimeMillis()-604800000)>date){
                val simpleDateFormat = SimpleDateFormat("MMM")
                time = simpleDateFormat.format(date)
            }else{
                val simpleDateFormat = SimpleDateFormat("hh:mm a")
                time = simpleDateFormat.format(date)
            }

            viewHolder.itemView.findViewById<TextView>(R.id.txtTimeStamp).text = time
            viewHolder.itemView.findViewById<TextView>(R.id.txtLatestmsg).text = chatMsg.text

        }

        override fun getLayout(): Int {
            return R.layout.latest_msg_row
        }

    }
    val adapter = GroupAdapter<GroupieViewHolder>()
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