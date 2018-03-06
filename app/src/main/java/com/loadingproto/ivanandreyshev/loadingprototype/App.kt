package com.loadingproto.ivanandreyshev.loadingprototype

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.loadingproto.ivanandreyshev.loadingprototype.data.DatabaseHelper

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var databaseHelper: DatabaseHelper
        lateinit var jobManager: JobManager
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        jobManager = JobManager(Configuration.Builder(this)
                .build())
    }
}
