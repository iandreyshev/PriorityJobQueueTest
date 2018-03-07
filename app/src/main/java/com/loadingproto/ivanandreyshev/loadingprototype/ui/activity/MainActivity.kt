package com.loadingproto.ivanandreyshev.loadingprototype.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter

import com.loadingproto.ivanandreyshev.loadingprototype.R
import com.loadingproto.ivanandreyshev.loadingprototype.App
import com.loadingproto.ivanandreyshev.loadingprototype.DownloadPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter.SimpleListAdapter
import com.loadingproto.ivanandreyshev.loadingprototype.ui.view.IMainView
import kotlinx.android.synthetic.main.activity_main.*

import org.jetbrains.anko.toast

class MainActivity : MvpAppCompatActivity(), IMainView {
    @InjectPresenter
    lateinit var mPresenter: DownloadPresenter
    private lateinit var mAdapter: SimpleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = SimpleListAdapter(mPresenter::onClick)
        list.adapter = mAdapter
        list.layoutManager = LinearLayoutManager(this)

        while (App.databaseHelper.contentItem.count() < 4) {
            App.databaseHelper.contentItem.create(ContentItem())
        }

        App.databaseHelper.contentItem.forEach { item ->
            mAdapter.insert(item)
        }
    }

    override fun toast(message: String) {
        toast(message.subSequence(0 until message.length))
    }
}
