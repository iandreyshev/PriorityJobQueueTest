package com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.arellomobile.mvp.MvpDelegate

abstract class MvpViewHolder<T>(
        private var mParentDelegate: MvpDelegate<*>,
        view: View) : RecyclerView.ViewHolder(view) {

    init {
        createMvpDelegate()
    }

    private var mMvpDelegate: MvpDelegate<*>? = null

    fun bind(data: T) {
        destroyMvpDelegate()
        onBind(data)
        createMvpDelegate()
    }

    protected abstract fun onBind(data: T)

    private fun getMvpDelegate(): MvpDelegate<*>? {
        if (getMvpChildId() == null) {
            return null
        }
        if (mMvpDelegate == null) {
            mMvpDelegate = MvpDelegate(this)
            mMvpDelegate?.setParentDelegate(mParentDelegate, getMvpChildId())
        }
        return mMvpDelegate
    }

    private fun destroyMvpDelegate() {
        if (getMvpDelegate() != null) {
            getMvpDelegate()?.onSaveInstanceState()
            getMvpDelegate()?.onDetach()
            mMvpDelegate = null
        }
    }

    private fun createMvpDelegate() {
        if (getMvpDelegate() != null) {
            getMvpDelegate()?.onCreate()
            getMvpDelegate()?.onAttach()
        }
    }

    protected abstract fun getMvpChildId(): String?
}
