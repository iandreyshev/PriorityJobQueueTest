package com.loadingproto.ivanandreyshev.loadingprototype.presentation.view

import com.arellomobile.mvp.MvpView
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem

interface IListView : MvpView {
    fun insertItem(item: ContentItem)

    fun removeItem(id: Int)
}
