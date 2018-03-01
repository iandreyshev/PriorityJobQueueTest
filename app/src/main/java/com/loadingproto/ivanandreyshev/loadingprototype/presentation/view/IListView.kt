package com.loadingproto.ivanandreyshev.loadingprototype.presentation.view

import com.arellomobile.mvp.MvpView

interface IListView : MvpView {
    fun insertItem(id: Int)

    fun removeItem(id: Int)
}
