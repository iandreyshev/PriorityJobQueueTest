package com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.IItemView
import org.greenrobot.eventbus.EventBus

@InjectViewState
class DownloadItemPresenter(private val mId: Int) : MvpPresenter<IItemView>() {
    init {
        EventBus.getDefault().register(this)
    }

    override fun onFirstViewAttach() {
        viewState.onAttach()
        viewState.enableProgress(false)
    }
}
