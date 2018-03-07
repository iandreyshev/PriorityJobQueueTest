package com.loadingproto.ivanandreyshev.loadingprototype

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.birbit.android.jobqueue.*
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
            .minConsumerCount(2)
            .maxConsumerCount(2)
            .loadFactor(1)
            .id("JobManager1")
            .build())

    fun onClick(item: ContentItem) {
        when (item.loadState) {
            LoadState.NOT_LOAD -> {
                mJobManager.cancelJobsInBackground(CancelResult.AsyncCancelCallback {
                    mJobManager.addJobInBackground(createLoadJob(item))
                }, TagConstraint.ALL, item.tag)
            }
            LoadState.WAITING -> {
                mJobManager.cancelJobsInBackground(CancelResult.AsyncCancelCallback {
                    item.loadState = LoadState.NOT_LOAD
                    EventBus.getDefault().post(UpdateItemEvent(item))
                }, TagConstraint.ALL, item.tag)
            }
            LoadState.IN_PROGRESS -> {
                mJobManager.cancelJobsInBackground(CancelResult.AsyncCancelCallback {
                    item.loadState = LoadState.NOT_LOAD
                    EventBus.getDefault().post(UpdateItemEvent(item))
                }, TagConstraint.ALL, item.tag)
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

    private fun createLoadJob(item: ContentItem): Job {
        val externalDir = App.context.getExternalFilesDir(null)
        val fileManager = FileManager(externalDir)
        val params = Params(0)
                .setSingleId(item.tag)
                .singleInstanceBy(item.tag)
                .addTags(item.tag)
                .persist()

        return LoadingJob(params, item, fileManager)
    }

    private val ContentItem.tag: String
        get() = id.toString()
}
