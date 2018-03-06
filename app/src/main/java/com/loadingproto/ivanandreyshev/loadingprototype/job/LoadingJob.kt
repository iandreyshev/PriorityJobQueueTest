package com.loadingproto.ivanandreyshev.loadingprototype.job

import android.util.Log
import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.LoadState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateLoadingProgressEvent
import com.loadingproto.ivanandreyshev.loadingprototype.fileManager.IFileManager
import com.loadingproto.ivanandreyshev.loadingprototype.fileManager.safeDelete
import org.greenrobot.eventbus.EventBus
import java.io.Serializable

class LoadingJob(
        private val item: ContentItem,
        tag: String,
        fileManager: IFileManager
) : Job(Params(0).clearTags().addTags(tag).singleInstanceBy(tag).persist()) {

    private val mCancelException = object : Throwable("Cancel task"), Serializable {}
    private val mTempFile = fileManager.createItemTempFile(item.id)
    private val mDestFile = fileManager.createItemFile(item.id)

    override fun onAdded() {
        item.loadState = LoadState.WAITING
        EventBus.getDefault().post(UpdateItemEvent(item))
    }

    override fun onRun() {
        item.loadState = LoadState.IN_PROGRESS
        EventBus.getDefault().post(UpdateItemEvent(item))

        repeat(5001) {
            // Any work here
            if (isCancelled) throw mCancelException

            Thread.sleep(1)
            item.loadProgress = it
            EventBus.getDefault().post(UpdateLoadingProgressEvent(item.loadProgress.toString()))
        }

        item.loadState = LoadState.DOWNLOADED
        EventBus.getDefault().post(UpdateItemEvent(item))
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        item.loadState = when (throwable) {
            mCancelException -> LoadState.NOT_LOAD
            else -> LoadState.NOT_LOAD
        }
        return RetryConstraint.CANCEL
    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {
        Log.e("TAG", "CANCEL")
        EventBus.getDefault().post(UpdateItemEvent(item))
        mTempFile.safeDelete()
    }
}
