package com.loadingproto.ivanandreyshev.loadingprototype.job

import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.data.LoadState
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

class LoadingJob(private val mItem: ContentItem, tag: String) : Job(Params(0).addTags(tag)) {

    companion object {
        private const val TIME_TO_STEP = 200
    }

    override fun onRun() {
        var lastTime = Calendar.getInstance().timeInMillis
        var progress = 0

        mItem.loadProgress = progress
        App.databaseHelper.contentItem.update(mItem)
        EventBus.getDefault().post(UpdateItemEvent(mItem))

        while (true) {
            if (Calendar.getInstance().timeInMillis - lastTime > TIME_TO_STEP) {
                lastTime = Calendar.getInstance().timeInMillis
                progress++

                mItem.loadProgress = progress
                mItem.loadState = if (progress >= 100) LoadState.READY else LoadState.IN_PROCESS
                App.databaseHelper.contentItem.update(mItem)
                EventBus.getDefault().post(UpdateItemEvent(mItem))

                if (progress >= 100) {
                    return
                }
            }
        }
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        return RetryConstraint.CANCEL
    }

    override fun onAdded() {

    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {
    }
}
