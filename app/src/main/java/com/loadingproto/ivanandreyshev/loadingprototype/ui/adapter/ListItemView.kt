package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.loadingproto.ivanandreyshev.loadingprototype.R
import kotlinx.android.synthetic.main.list_item_view.view.*
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.OfflineState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemLoadingEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ListItemView(context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.list_item_view, this, true)
        EventBus.getDefault().register(this)
    }

    var onLoadListener: (ContentItem) -> Unit = {}
    var onDeleteListener: (ContentItem) -> Unit = {}

    private var mItem: ContentItem = ContentItem()

    fun bind(item: ContentItem) {
        mItem = item
        idView.text = mItem.id.toString()

        loadButton.setOnClickListener { onLoadListener(mItem) }
        deleteButton.setOnClickListener { onDeleteListener(mItem) }
        progress.setVisible(item.loadState == OfflineState.IN_PROCESS)

        loadButton.text = when (item.loadState) {
            OfflineState.WAIT_LOAD -> "WAIT_LOAD"
            OfflineState.NOT_LOAD -> "LOAD"
            OfflineState.IN_PROCESS -> "STOP"
            OfflineState.NOT_AVAILABLE -> "NOT"
            OfflineState.READY -> "REM"
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateProgress(event: UpdateItemLoadingEvent) {
        if (mItem.id == event.id) {
            progress.text = event.progress.toString()
        }
    }

    private fun View.setVisible(value: Boolean) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }
}
