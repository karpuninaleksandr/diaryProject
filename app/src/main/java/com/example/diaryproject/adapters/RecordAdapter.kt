package com.example.diaryproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diaryproject.R
import com.example.diaryproject.Utils
import com.example.diaryproject.database.DiaryRecord

class RecordViewHolder(itemView: View, private var onItemClick: ((DiaryRecord) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
    private val recordValue: TextView = itemView.findViewById(R.id.item_record_value)
    private val recordTags: TextView = itemView.findViewById(R.id.item_record_tags)
    private val recordLastChangeDate: TextView = itemView.findViewById(R.id.item_record_last_change_time)

    fun bind(item: DiaryRecord) {
        recordValue.text = item.recordValue
        recordTags.text = item.tags
        recordLastChangeDate.text = Utils.convertLongToDateString(item.lastChangeTimeMillis)
        setClickListener(item)
    }

    private fun setClickListener(item: DiaryRecord) {
        itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    companion object {
        fun from(parent: ViewGroup, onItemClick: ((DiaryRecord) -> Unit)?): RecordViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_record, parent, false)
            return RecordViewHolder(view, onItemClick)
        }
    }
}

class RecordAdapter : RecyclerView.Adapter<RecordViewHolder>() {

    var data = listOf<DiaryRecord>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((DiaryRecord) -> Unit)? = null

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder.from(parent, onItemClick)
    }
}