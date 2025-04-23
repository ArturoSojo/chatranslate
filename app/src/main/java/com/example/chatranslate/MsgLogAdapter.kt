package com.example.chatranslate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatranslate.databinding.MsgLogItemBinding

class MsgLogAdapter(private var msgList: List<String>) :
    RecyclerView.Adapter<MsgLogAdapter.MsgLogViewHolder>() {

    inner class MsgLogViewHolder(private val binding: MsgLogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(msg: String) {
            binding.msgTextView.text = msg
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgLogViewHolder {
        val binding = MsgLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MsgLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MsgLogViewHolder, position: Int) {
        holder.bind(msgList[position])
    }

    override fun getItemCount(): Int = msgList.size

    fun updateData(newList: List<String>) {
        msgList = newList
        notifyDataSetChanged()
    }
}
