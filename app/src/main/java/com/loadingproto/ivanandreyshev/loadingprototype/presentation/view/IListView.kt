package com.loadingproto.ivanandreyshev.loadingprototype.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem

interface IListView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun toast(message: String)
}
