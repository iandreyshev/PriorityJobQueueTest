package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.content.Context
import com.birbit.android.jobqueue.CancelResult
import com.birbit.android.jobqueue.TagConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.job.LoadingJob

class ListAdapter(private val mContext: Context) : BaseAdapter() {

    var onLoadListener: (Int) -> Unit = {}

    private val mList = ArrayList<ContentItem>()

    fun insertItem(item: ContentItem) {
        mList.add(item)
        notifyDataSetChanged()
    }

    fun removeItem(id: Int) {
        mList.removeAt(mList.indexOfFirst { id == it.id })
        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val resultView = view ?: ListItemView(mContext)
        (resultView as ListItemView).bind(mList[position])

        resultView.onLoadListener = { id ->
            val tag = id.toString()
            App.jobManager.cancelJobsInBackground(CancelResult.AsyncCancelCallback {}, TagConstraint.ALL, tag)
            App.jobManager.addJobInBackground(LoadingJob(getItem(id), tag))
        }
        resultView.onDeleteListener = { id ->
            App.databaseHelper.contentItem.delete(getItem(id))
            mList.remove(getItem(id))
            notifyDataSetChanged()
        }

        return resultView
    }

    override fun getItem(position: Int): ContentItem = mList[position]

    override fun getItemId(position: Int): Long = mList[position].id.toLong()

    override fun getCount(): Int = mList.size
}
