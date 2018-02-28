package com.loadingproto.ivanandreyshev.loadingprototype.presentation.view

import com.arellomobile.mvp.MvpView

interface ListView : MvpView {
    fun insertItem(id: Long)

    fun removeItem(id: Long)
}
