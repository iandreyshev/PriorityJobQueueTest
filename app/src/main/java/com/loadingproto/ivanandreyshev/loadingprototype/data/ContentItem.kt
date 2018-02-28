package com.loadingproto.ivanandreyshev.loadingprototype.data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "content", daoClass = ContentItemDaoImpl::class)
data class ContentItem(
        @DatabaseField(columnName = "id_item", generatedId = true)
        var id: Long = 0,
        @DatabaseField(columnName = "load_state", generatedId = false)
        var loadState: LoadState = LoadState.NOT_LOAD,
        @DatabaseField(columnName = "isFavorite", generatedId = false)
        val isFavorite: Boolean = false
)
