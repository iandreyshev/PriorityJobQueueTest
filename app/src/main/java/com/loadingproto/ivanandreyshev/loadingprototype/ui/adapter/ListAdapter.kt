package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpDelegate
import com.loadingproto.ivanandreyshev.loadingprototype.R

class ListAdapter(private val mParentDelegate: MvpDelegate<*>) : RecyclerView.Adapter<ListViewHolder>() {

    var onLoadListener: (Int) -> Unit = {}
    var onClearListener: (Int) -> Unit = {}
    var onFavoriteListener: (Int) -> Unit = {}
    var onDeleteListener: (Int) -> Unit = {}

    private val mList = ArrayList<Int>()

    fun insertItem(id: Int) {
        mList.add(id)
        notifyDataSetChanged()
    }

    fun removeItem(id: Int) {
        mList.remove(id)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onLoadListener = onLoadListener
        holder.onClearListener = onClearListener
        holder.onFavoriteListener = onFavoriteListener
        holder.onDeleteListener = onDeleteListener
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)

        return ListViewHolder(mParentDelegate, view)
    }

    override fun getItemCount(): Int = mList.size
}
