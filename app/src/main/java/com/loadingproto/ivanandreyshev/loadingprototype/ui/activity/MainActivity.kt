package com.loadingproto.ivanandreyshev.loadingprototype.ui.activity

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter

import com.loadingproto.ivanandreyshev.loadingprototype.R
import com.loadingproto.ivanandreyshev.loadingprototype.App
import com.loadingproto.ivanandreyshev.loadingprototype.DownloadPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.LoadState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateLoadingProgressEvent
import com.loadingproto.ivanandreyshev.loadingprototype.ui.view.IMainView
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class MainActivity : MvpAppCompatActivity(), IMainView {

    @InjectPresenter
    lateinit var mPresenter: DownloadPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)

        if (App.databaseHelper.contentItem.count() == 0) {
            App.databaseHelper.contentItem.create(ContentItem(url = ""))
        }

        val item = App.databaseHelper.contentItem.first()
        stateView.text = item.loadState.name
        progressView.text = if (item.loadState == LoadState.IN_PROGRESS) item.loadProgress.toString() else ""

        button.setOnClickListener {
            mPresenter.onClick(App.databaseHelper.contentItem.first())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateProgress(event: UpdateLoadingProgressEvent) {
        progressView.text = event.progress
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateItem(event: UpdateItemEvent) {
        App.databaseHelper.contentItem.update(event.item)
        stateView.text = App.databaseHelper.contentItem.first().loadState.name
    }

    override fun toast(message: String) {
        toast(message.subSequence(0 until message.length))
    }
}
