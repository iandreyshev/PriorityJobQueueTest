package com.loadingproto.ivanandreyshev.loadingprototype.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter

import com.loadingproto.ivanandreyshev.loadingprototype.R
import com.loadingproto.ivanandreyshev.loadingprototype.app.App
import com.loadingproto.ivanandreyshev.loadingprototype.data.ContentItem
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter.ListPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.IListView
import com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), IListView {

    @InjectPresenter
    lateinit var mPresenter: ListPresenter

    private lateinit var mListAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListAdapter = ListAdapter(this, { mPresenter.onLoadingButtonClick(it) })
        mListAdapter.updateItems(App.databaseHelper.contentItem.asIterable())

        itemsListView.adapter = mListAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun toast(message: String) {
        toast(message)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.option_create) {
            App.databaseHelper.contentItem.create(ContentItem())
            mListAdapter.updateItems(App.databaseHelper.contentItem.asIterable())
        }

        return super.onOptionsItemSelected(item)
    }
}
