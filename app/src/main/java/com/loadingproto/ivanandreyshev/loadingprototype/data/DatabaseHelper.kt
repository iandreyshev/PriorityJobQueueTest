package com.loadingproto.ivanandreyshev.loadingprototype.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.loadingproto.ivanandreyshev.loadingprototype.event.UpdateItemEvent
import org.greenrobot.eventbus.EventBus

class DatabaseHelper(
        context: Context
) : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
        IDatabase {

    companion object {
        private const val DATABASE_NAME = "database.db"
        private const val DATABASE_VERSION = 1
    }

    val contentItem: Dao<ContentItem, Int>
        get() = getDao(ContentItem::class.java)

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        TableUtils.createTable(connectionSource, ContentItem::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
    }

    @Synchronized
    override fun updateItem(item: ContentItem) {
        contentItem.update(item)
        EventBus.getDefault().post(UpdateItemEvent(item))
    }
}
