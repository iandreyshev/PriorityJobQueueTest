package com.loadingproto.ivanandreyshev.loadingprototype.data

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource

import java.sql.SQLException

class ContentItemDaoImpl @Throws(SQLException::class)

constructor(connectionSource: ConnectionSource) : BaseDaoImpl<ContentItem, Int>(connectionSource, ContentItem::class.java), ContentItemDao {
    init {
        setObjectCache(true)
    }
}
