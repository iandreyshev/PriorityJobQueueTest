package com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.ListView

@InjectViewState
class ListPresenter : MvpPresenter<ListView>() {

    companion object {
        private const val MAX_COUNT: Int = 20
    }

    private var mCount: Int = 0
    private var mLast: Long = 0

    fun onAdd() {
        if (mCount == MAX_COUNT) {
            return
        }

        viewState.insertItem(mLast)
        mLast++
        mCount++
    }

    fun onRemove(id: Long) {
        viewState.removeItem(id)
    }
}
