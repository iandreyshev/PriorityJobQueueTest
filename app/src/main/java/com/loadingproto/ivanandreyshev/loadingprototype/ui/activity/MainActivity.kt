package com.loadingproto.ivanandreyshev.loadingprototype.ui.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.R
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.presenter.ListPresenter
import com.loadingproto.ivanandreyshev.loadingprototype.presentation.view.IListView
import com.loadingproto.ivanandreyshev.loadingprototype.ui.adapter.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), IListView {
    @InjectPresenter
    lateinit var mPresenter: ListPresenter

    private val mListAdapter = ListAdapter(mvpDelegate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListAdapter.onLoadListener

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.all -> mPresenter.showAll()
                R.id.local -> mPresenter.showLocal()
                R.id.favorites -> mPresenter.showFavorites()
                R.id.downloading -> mPresenter.showDownloading()
            }
            return@setOnNavigationItemSelectedListener true
        }

        itemsListView.adapter = mListAdapter
        itemsListView.layoutManager = LinearLayoutManager(this)
        itemsListView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.option_create) {
            mPresenter.onAdd()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun insertItem(id: Int) {
        mListAdapter.insertItem(id)
    }

    override fun removeItem(id: Int) {
        mListAdapter.removeItem(id)
    }
}
