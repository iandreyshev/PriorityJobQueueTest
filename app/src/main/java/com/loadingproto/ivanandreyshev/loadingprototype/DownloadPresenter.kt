package com.loadingproto.ivanandreyshev.loadingprototype

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.birbit.android.jobqueue.CancelResult
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.TagConstraint
import com.birbit.android.jobqueue.config.Configuration
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.LoadState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import com.loadingproto.ivanandreyshev.loadingprototype.fileManager.FileManager
import com.loadingproto.ivanandreyshev.loadingprototype.job.LoadingJob
import com.loadingproto.ivanandreyshev.loadingprototype.ui.view.IMainView
import org.greenrobot.eventbus.EventBus

@InjectViewState
class DownloadPresenter : MvpPresenter<IMainView>() {

    private val mJobManager = JobManager(Configuration.Builder(App.context)
            .maxConsumerCount(10)
            .minConsumerCount(2)
            .build())

    fun onClick(item: ContentItem) = when (item.loadState) {
        LoadState.NOT_LOAD -> {
            mJobManager.cancelJobsInBackground(CancelResult.AsyncCancelCallback {
                val externalDir = App.context.getExternalFilesDir(null)
                val fileManager = FileManager(externalDir)
                mJobManager.addJobInBackground(LoadingJob(item, item.id.toString(), fileManager))
            }, TagConstraint.ALL, item.id.toString())
        }

        LoadState.WAITING -> {
            mJobManager.cancelJobsInBackground(CancelResult.AsyncCancelCallback {
                item.loadState = LoadState.NOT_LOAD
                EventBus.getDefault().post(UpdateItemEvent(item))
            }, TagConstraint.ALL, item.id.toString())
        }

        LoadState.IN_PROGRESS -> {
            mJobManager.cancelJobsInBackground(CancelResult.AsyncCancelCallback {
                item.loadState = LoadState.NOT_LOAD
                EventBus.getDefault().post(UpdateItemEvent(item))
            }, TagConstraint.ALL, item.id.toString())
        }

        LoadState.NOT_AVAILABLE -> {
            viewState.toast("Downloading not available")
        }

        LoadState.DOWNLOADED -> {
            item.loadState = LoadState.NOT_LOAD
            EventBus.getDefault().post(UpdateItemEvent(item))
            viewState.toast("Deleted")
        }
    }
}
