package com.fp.golink.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.fp.golink.R

class HistoryAdapter(private val context: Context, private val data: List<HistoryItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.history_item, parent, false)

        val history_link: TextView = view.findViewById(R.id.tv_history_link)
        val history_result: TextView = view.findViewById(R.id.tv_history_result)

        if(data[position].link == "1") {
            history_link.text = "Aman"
        }
        else {
            history_link.text = "Tidak Aman"
        }

        history_result.text = data[position].result

        return view
    }
}
