package com.loadingproto.ivanandreyshev.loadingprototype.job

import android.util.Log
import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.OfflineState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemLoadingEvent
import com.loadingproto.ivanandreyshev.loadingprototype.web.GetRequestHandler
import com.loadingproto.ivanandreyshev.loadingprototype.web.HttpRequestHandler
import com.loadingproto.ivanandreyshev.loadingprototype.web.IProgressListener
import org.greenrobot.eventbus.EventBus

class LoadingUseCase(
        tag: String,
        private val mItem: ContentItem
) : Job(Params(0).addTags(tag).persist()) {

    override fun onAdded() {
        updateItemLoadState(OfflineState.WAIT_LOAD)
    }

    override fun onRun() {
        if (isCancelled) throw RuntimeException("")

        updateItemLoadState(OfflineState.IN_PROCESS)

        val requestHandler = GetRequestHandler()
        requestHandler.progressListener = object : IProgressListener {
            override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                Log.e("LEN", contentLength.toString())
                Log.e("READ", bytesRead.toString())

                if (isCancelled) throw RuntimeException("")

                val progress = (contentLength / bytesRead).toInt()
                EventBus.getDefault().post(UpdateItemLoadingEvent(mItem.id, progress))
            }
        }

        val state = requestHandler.send("https://www.ispring.ru/download/demos/russia-and-cosmos.zip")

        if (state == HttpRequestHandler.State.SUCCESS) {
            updateItemLoadState(OfflineState.READY)
        } else {
            updateItemLoadState(OfflineState.NOT_LOAD)
        }
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        return RetryConstraint.CANCEL
    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {
        updateItemLoadState(OfflineState.NOT_LOAD)
    }

    private fun updateItemLoadState(newState: OfflineState) {
        mItem.loadState = newState
        App.databaseHelper.contentItem.update(mItem)
        EventBus.getDefault().post(UpdateItemEvent(mItem))
    }
}
