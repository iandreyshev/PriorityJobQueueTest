package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.loadingproto.ivanandreyshev.loadingprototype.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.LoadState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateProgressEvent
import kotlinx.android.synthetic.main.list_item.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.runOnUiThread

class ListItem(view: View) : RecyclerView.ViewHolder(view) {
    private var mItem: ContentItem? = null

    init {
        EventBus.getDefault().register(this)
    }

    fun setClickListener(listener: (ContentItem) -> Unit) {
        itemView.button.setOnClickListener {
            listener(mItem ?: return@setOnClickListener)
        }
    }

    fun bind(item: ContentItem) {
        mItem = item
        itemView.progressBar.max = 100
        itemView.progressBar.progress = if (item.loadState == LoadState.IN_PROGRESS) item.loadProgress else 0
        Log.e(javaClass.name, itemView.progressBar.progress.toString())
        itemView.button.text = item.loadState.name
    }

    @Subscribe
    fun updateProgress(event: UpdateProgressEvent) {
        if (event.item.id == mItem?.id) {
            App.databaseHelper.contentItem.update(event.item)
            App.context.runOnUiThread {
                bind(event.item)
            }
        }
    }

    @Subscribe
    fun updateItem(event: UpdateItemEvent) {
        if (event.item.id == mItem?.id) {
            App.databaseHelper.contentItem.update(event.item)
            App.context.runOnUiThread {
                bind(event.item)
            }
        }
    }
}
