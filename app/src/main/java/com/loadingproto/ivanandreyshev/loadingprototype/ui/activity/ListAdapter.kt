package com.loadingproto.ivanandreyshev.loadingprototype.ui.activity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.loadingproto.ivanandreyshev.loadingprototype.R

class ListAdapter : RecyclerView.Adapter<ListItem>() {
    private val mList = ArrayList<Long>()
    private var mOnDeleteListener: ((Long) -> Unit)? = null

    fun insertItem(id: Long) {
        mList.add(id)
        notifyDataSetChanged()
    }

    fun removeItem(id: Long) {
        mList.remove(id)
        notifyDataSetChanged()
    }

    fun setOnDeleteListener(listener: (Long) -> Unit) {
        mOnDeleteListener = listener
    }

    override fun onBindViewHolder(holder: ListItem, position: Int) {
        holder.bind(mList[position], mOnDeleteListener ?: {})
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItem {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)

        return ListItem(view)
    }

    override fun getItemCount(): Int = mList.size
}
