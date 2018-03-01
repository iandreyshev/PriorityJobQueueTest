package com.loadingproto.ivanandreyshev.loadingprototype.app

import android.app.Application
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.loadingproto.ivanandreyshev.loadingprototype.data.DatabaseHelper

class App : Application() {
    companion object {
        lateinit var databaseHelper: DatabaseHelper
        lateinit var jobManager: JobManager
    }

    override fun onCreate() {
        super.onCreate()
        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        jobManager = JobManager(Configuration.Builder(this)
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .build())
    }
}
