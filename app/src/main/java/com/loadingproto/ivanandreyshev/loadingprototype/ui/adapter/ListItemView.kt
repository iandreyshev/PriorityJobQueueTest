package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.loadingproto.ivanandreyshev.loadingprototype.R
import kotlinx.android.synthetic.main.list_item_view.view.*
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.LoadState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ListItemView(context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.list_item_view, this, true)
        EventBus.getDefault().register(this)
    }

    var onLoadListener: (Int) -> Unit = {}
    var onDeleteListener: (Int) -> Unit = {}

    private var mId: Int = 0

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateItem(event: UpdateItemEvent) {
        if (mId == event.item.id) {
            bind(event.item)
        }
    }

    fun bind(item: ContentItem) {
        mId = item.id
        idView.text = mId.toString()

        loadButton.setOnClickListener { onLoadListener(mId) }
        deleteButton.setOnClickListener { onDeleteListener(mId) }

        progress.text = item.loadProgress.toString()
        progress.setVisible(item.loadState == LoadState.IN_PROCESS)

        when (item.loadState) {
            LoadState.NOT_LOAD -> loadButton.text = "LOAD"
            LoadState.IN_PROCESS -> loadButton.text = "STOP"
            LoadState.FAILED -> loadButton.text = "TRY"
            LoadState.NOT_AVAILABLE -> loadButton.text = "NOT"
            LoadState.READY -> loadButton.text = "REM"
        }
    }

    private fun View.setVisible(value: Boolean) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }
}
