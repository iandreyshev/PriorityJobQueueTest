package com.loadingproto.ivanandreyshev.loadingprototype.ui.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.R
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter.ListPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.ListView
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : MvpAppCompatActivity(), ListView {
    @InjectPresenter
    lateinit var mListPresenter: ListPresenter

    private val mListAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        mListAdapter.setOnDeleteListener {
            mListPresenter.onRemove(it)
        }

        itemsListView.adapter = mListAdapter
        itemsListView.layoutManager = LinearLayoutManager(this)
        itemsListView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        addButton.setOnClickListener {
            mListPresenter.onAdd()
        }
    }

    override fun insertItem(id: Long) = mListAdapter.insertItem(id)

    override fun removeItem(id: Long) {
        mListAdapter.removeItem(id)
    }
}
