package com.example.diaryproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diaryproject.R
import com.example.diaryproject.database.DiaryTag

class TagViewHolder private constructor(itemView: View, private var onItemClick: ((DiaryTag) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
    private val tagName: TextView = itemView.findViewById(R.id.tag_name)

    fun bind(item: DiaryTag) {
        tagName.text = item.tagName
        setClickListener(item)
    }

    private fun setClickListener(item: DiaryTag) {
        itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    companion object {
        fun from(parent: ViewGroup, onItemClick: ((DiaryTag) -> Unit)?): TagViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_tag, parent, false)
            return TagViewHolder(view, onItemClick)
        }
    }
}

class TagAdapter : RecyclerView.Adapter<TagViewHolder>() {

    var data = listOf<DiaryTag>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((DiaryTag) -> Unit)? = null

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder.from(parent, onItemClick)
    }
}