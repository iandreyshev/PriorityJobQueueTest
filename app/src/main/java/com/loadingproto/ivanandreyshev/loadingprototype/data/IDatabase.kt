package com.loadingproto.ivanandreyshev.loadingprototype.data

import java.io.Serializable

interface IDatabase : Serializable {
    fun updateItem(item: ContentItem)
}
