package com.loadingproto.ivanandreyshev.loadingprototype.job

import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateLoadingProgressEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

class LoadingJob(private val mId: Int) : Job(Params(0)) {

    companion object {
        private const val TIME_TO_STEP = 200
    }

    override fun onRun() {
        var lastTime = Calendar.getInstance().timeInMillis
        var progress = 0

        while (true) {
            if (Calendar.getInstance().timeInMillis - lastTime > TIME_TO_STEP) {
                lastTime = Calendar.getInstance().timeInMillis
                progress++
                EventBus.getDefault().post(UpdateLoadingProgressEvent(mId, progress))

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
