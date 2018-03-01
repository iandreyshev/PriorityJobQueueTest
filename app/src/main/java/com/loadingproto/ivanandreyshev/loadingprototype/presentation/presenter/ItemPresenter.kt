package com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateLoadingProgressEvent
import com.loadingproto.ivanandreyshev.loadingprototype.job.LoadingJob
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.IItemView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@InjectViewState
class ItemPresenter(private val mId: Int) : MvpPresenter<IItemView>() {
    override fun onFirstViewAttach() {
        EventBus.getDefault().register(this)
        viewState.update(0)
    }

    fun start() {
        App.jobManager.addJobInBackground(LoadingJob(mId))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun upd(event: UpdateLoadingProgressEvent) {
        if (event.id == mId) {
            viewState.update(event.progress)
        }
    }
}
