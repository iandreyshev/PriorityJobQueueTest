package com.loadingproto.ivanandreyshev.loadingprototype

import android.app.Application
import android.util.Log
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.loadingproto.ivanandreyshev.loadingprototype.data.DatabaseHelper

class App : Application() {
    companion object {
        lateinit var databaseHelper: DatabaseHelper
    }

    override fun onCreate() {
        super.onCreate()
        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)

        Log.e("Tag", databaseHelper.contentItem.isTableExists.toString())
    }
}
