package com.loadingproto.ivanandreyshev.loadingprototype.ui.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface IMainView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun toast(message: String)
}
