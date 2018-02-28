package com.loadingproto.ivanandreyshev.loadingprototype.job

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import org.jetbrains.anko.doAsync

class LoadingJob : JobService() {
    companion object {
        const val ID_KEY = "ID"
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        doAsync {
            load(job?.extras?.getLong(ID_KEY) ?: return@doAsync)
        }

        return false
    }

    override fun onStartJob(job: JobParameters?): Boolean {
        return false
    }

    private fun load(id: Long) {

    }
}
