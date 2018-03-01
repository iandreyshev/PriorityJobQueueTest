package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.view.View
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter.DownloadItemPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.IItemView
import kotlinx.android.synthetic.main.list_item_view.view.*

class ListViewHolder(
        parentDelegate: MvpDelegate<*>,
        view: View) : MvpViewHolder<Int>(parentDelegate, view), IItemView {
    override fun onAttach() {
    }

    override fun enableProgress(isEnable: Boolean) {
    }

    @InjectPresenter
    lateinit var mPresenter: DownloadItemPresenter

    @ProvidePresenter
    fun provideItemPresenter(): DownloadItemPresenter {
        return DownloadItemPresenter(mId)
    }

    override fun updateProgress(value: Int) {
        itemView.progress.text = value.toString()
    }

    var onLoadListener: (Int) -> Unit = {}
    var onClearListener: (Int) -> Unit = {}
    var onFavoriteListener: (Int) -> Unit = {}
    var onDeleteListener: (Int) -> Unit = {}

    private var mId: Int = 0

    override fun onBind(data: Int) {
        mId = data
        itemView.idView.text = mId.toString()
        //itemView.loadButton.setOnClickListener { mPresenter.start() }
        //itemView.clearButton.setOnClickListener { onClearListener(mId) }
        //itemView.favoriteButton.setOnClickListener { onFavoriteListener(mId) }
        itemView.deleteButton.setOnClickListener { onDeleteListener(mId) }
    }

    override fun getMvpChildId(): String? {
        return mId.toString()
    }
}
