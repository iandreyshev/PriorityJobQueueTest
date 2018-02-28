package com.loadingproto.ivanandreyshev.loadingprototype.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.android.AndroidConnectionSource
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

class DatabaseHelper(context: Context) : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "database.db"
        private const val DATABASE_VERSION = 7
    }

    var connectionSource: AndroidConnectionSource?
        get() = super.connectionSource
        set(value) {
            super.connectionSource = value
        }
    val contentItem: ContentItemDao
        get() = getDao(ContentItem::class.java)

    private var mContentItemDao: ContentItemDao? = null

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        TableUtils.createTable(connectionSource, ContentItem::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        for (version in oldVersion + 1..newVersion) {
            when (version) {
                5 -> database.execSQL("ALTER TABLE content ADD COLUMN favorite")
                6 -> database.execSQL("ALTER TABLE content ADD COLUMN params")
                7 -> {
                    database.execSQL("ALTER TABLE content ADD COLUMN private")
                    database.execSQL("ALTER TABLE content ADD COLUMN download_url_provider")
                    database.execSQL("ALTER TABLE content ADD COLUMN view_url")
                    database.execSQL("ALTER TABLE content ADD COLUMN thubmnail_url")
                    database.execSQL("ALTER TABLE content ADD COLUMN dynamic")
                    database.execSQL("ALTER TABLE content ADD COLUMN outdated")
                    database.execSQL("ALTER TABLE content ADD COLUMN hasBlur")
                }
            }
        }
    }
}
