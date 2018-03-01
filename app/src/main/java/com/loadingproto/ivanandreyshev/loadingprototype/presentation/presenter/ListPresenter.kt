package com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.IListView

@InjectViewState
class ListPresenter : MvpPresenter<IListView>() {

    companion object {
        private const val MAX_COUNT: Int = 20
    }

    override fun onFirstViewAttach() {
        App.databaseHelper.contentItem.forEach {
            viewState.insertItem(it.id)
        }
    }

    fun onAdd() {
        val newItem = ContentItem()
        App.databaseHelper.contentItem.create(newItem)
        viewState.insertItem(newItem.id)
    }

    fun onRemove(id: Int) {
        viewState.removeItem(id)
    }

    fun showAll() {

    }

    fun showLocal() {

    }

    fun showFavorites() {

    }

    fun showDownloading() {

    }
}
