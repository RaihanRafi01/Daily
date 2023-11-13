package com.example.daily.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.daily.R
import com.example.daily.databinding.ActivityChatLogBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatLogActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = username

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatFromItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        binding.recyChatLog.adapter = adapter


    }
}
class ChatFromItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChatToItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}