package com.loadingproto.ivanandreyshev.loadingprototype.ui.activity

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item.view.*

class ListItem(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(id: Long, onDeleteListener: (Long) -> Unit) {
        itemView.idView.text = id.toString()
        itemView.deleteButton.setOnClickListener {
            onDeleteListener(id)
        }
        itemView.progressBar.isIndeterminate = false
        itemView.progressBar.progress = 25
    }
}
