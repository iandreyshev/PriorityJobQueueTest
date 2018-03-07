package com.loadingproto.ivanandreyshev.loadingprototype.job

import android.util.Log
import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.LoadState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateProgressEvent
import com.loadingproto.ivanandreyshev.loadingprototype.fileManager.IFileManager
import org.greenrobot.eventbus.EventBus
import java.io.Serializable

class LoadingJob(
        params: Params,
        private val item: ContentItem,
        fileManager: IFileManager
) : Job(params) {
    private val mCancelException = object : Throwable(), Serializable {}
    private val mContentDir = fileManager.createItemFile(item.id)
    private val mTempDir = fileManager.createItemTempFile(item.id)

    override fun onAdded() {
        notifyItemLoadStateChanged(LoadState.WAITING)
        Log.e(javaClass.name, "Start with id $id")
    }

    override fun onRun() {
        notifyItemLoadStateChanged(LoadState.IN_PROGRESS)

        repeat(101) { progress ->
            Thread.sleep(42)
            if (isCancelled) throw mCancelException
            notifyItemLoadProgressChanged(progress)
        }

        notifyItemLoadStateChanged(LoadState.DOWNLOADED)
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        Log.e("Throwable name", throwable.javaClass.name)
        notifyItemLoadStateChanged(LoadState.NOT_LOAD)
        return RetryConstraint.CANCEL
    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {
        Log.e(javaClass.name, "CANCELED")
    }

    private fun notifyItemLoadStateChanged(newState: LoadState) {
        item.loadState = newState
        EventBus.getDefault().post(UpdateItemEvent(item))
    }

    private fun notifyItemLoadProgressChanged(newProgress: Int) {
        item.loadProgress = newProgress
        EventBus.getDefault().post(UpdateProgressEvent(item))
    }
}
