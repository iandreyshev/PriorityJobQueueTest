package com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.birbit.android.jobqueue.TagConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.OfflineState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import com.loadingproto.ivanandreyshev.loadingprototype.job.LoadingUseCase
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.IListView
import org.greenrobot.eventbus.EventBus

@InjectViewState
class ListPresenter : MvpPresenter<IListView>() {
    fun onLoadingButtonClick(item: ContentItem) {
        when (item.loadState) {
            OfflineState.NOT_LOAD -> onDownloadItem(item)
            OfflineState.WAIT_LOAD -> onStopDownloading(item)
            OfflineState.IN_PROCESS -> onStopDownloading(item)
            OfflineState.NOT_AVAILABLE -> onTryToDownloadInvalidItem(item)
            OfflineState.READY -> onDeleteItemData(item)
        }
    }

    private fun onDownloadItem(item: ContentItem) {
        onStopDownloading(item)
        App.jobManager.addJobInBackground(LoadingUseCase(item.downloadJobTag, item))
    }

    private fun onStopDownloading(item: ContentItem) {
        item.loadState = OfflineState.NOT_LOAD
        App.jobManager.cancelJobsInBackground(null, TagConstraint.ALL, item.downloadJobTag)
        EventBus.getDefault().post(UpdateItemEvent(item))
    }

    private fun onTryToDownloadInvalidItem(item: ContentItem) {
        viewState.toast("NOT AVAILABLE")
    }

    private fun onDeleteItemData(item: ContentItem) {
        item.loadState = OfflineState.NOT_LOAD
        App.databaseHelper.contentItem.update(item)
        EventBus.getDefault().post(UpdateItemEvent(item))
    }

    private val ContentItem.downloadJobTag: String
        get() = "Download $id"
}
