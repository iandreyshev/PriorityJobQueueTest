package com.loadingproto.ivanandreyshev.loadingprototype.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface IItemView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun update(value: Int)
}
