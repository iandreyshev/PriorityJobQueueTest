package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.content.Context
import android.util.Log
import com.birbit.android.jobqueue.TagConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ListAdapter(
        private val mContext: Context,
        private val onLoadListener: (ContentItem) -> Unit
) : BaseAdapter() {

    private var mList = ArrayList<ContentItem>()

    init {
        EventBus.getDefault().register(this)
    }

    fun updateItems(items: Iterable<ContentItem>) {
        mList = ArrayList()
        mList.addAll(items)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val resultView = view ?: ListItemView(mContext)
        (resultView as ListItemView).bind(mList[position])

        resultView.onLoadListener = { onLoadListener(it) }
        resultView.onDeleteListener = { item ->
            App.jobManager.cancelJobsInBackground(null, TagConstraint.ALL, item.id.toString())
            App.databaseHelper.contentItem.delete(item)
            mList.remove(getItem(item.id))
            notifyDataSetChanged()
        }

        return resultView
    }

    override fun getItem(id: Int): ContentItem {
        val item = mList.find {
            it.id == id
        }

        return if (item == null) {
            Log.e(javaClass.name, "Item not found")
            ContentItem()
        } else {
            item
        }
    }

    override fun getItemId(position: Int): Long = mList[position].id.toLong()

    override fun getCount(): Int = mList.size

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateItem(event: UpdateItemEvent) {
        mList.forEachIndexed { index, contentItem ->
            if (contentItem.id == event.item.id) {
                mList[index] = event.item
                notifyDataSetChanged()
                return@forEachIndexed
            }
        }
    }
}
