package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.loadingproto.ivanandreyshev.loadingprototype.R
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem

class SimpleListAdapter(
        private val clickListener: (ContentItem) -> Unit
) : RecyclerView.Adapter<ListItem>() {
    private val mList = ArrayList<ContentItem>()

    fun insert(item: ContentItem) {
        mList.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListItem {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
        return ListItem(view)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: ListItem?, position: Int) {
        holder?.bind(mList[position])
        holder?.setClickListener(clickListener)
    }
}
