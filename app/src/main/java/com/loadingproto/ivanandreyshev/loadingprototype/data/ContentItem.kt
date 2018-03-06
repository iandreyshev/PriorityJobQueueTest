package com.loadingproto.ivanandreyshev.loadingprototype.data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.io.Serializable

@DatabaseTable(tableName = "content", daoClass = ContentItemDaoImpl::class)
data class ContentItem(
        @DatabaseField(columnName = "id_item", generatedId = true)
        var id: Int = 0,
        @DatabaseField(columnName = "load_state")
        var loadState: LoadState = LoadState.NOT_LOAD,
        @DatabaseField(columnName = "load_progress")
        var loadProgress: Int = 0,
        @DatabaseField(columnName = "url", canBeNull = true)
        val url: String? = null
) : Serializable
