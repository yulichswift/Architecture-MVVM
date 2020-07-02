package com.jeff.architecture_mvvm.view.github

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import com.jeff.architecture_mvvm.view.base.BaseViewModel
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.view.github.paging.UserDataSource
import com.jeff.architecture_mvvm.view.github.paging.UserFactory
import com.log.JFLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.inject

class GitHubViewModel : BaseViewModel() {

    private val apiRepository: ApiRepository by inject()

    private val _userListData = MutableLiveData<PagedList<UserItem>>()
    val userListData: LiveData<PagedList<UserItem>> = _userListData

    fun getUsers() {
        viewModelScope.launch {
            getPagingItems().asFlow().collect {
                _userListData.postValue(it)
            }
        }
    }

    private fun getPagingItems(): LiveData<PagedList<UserItem>> {
        val userDataSource = UserDataSource(viewModelScope, apiRepository, callback)
        val userFactory = UserFactory(userDataSource)
        val config = PagedList.Config.Builder().setPageSize(20).build()
        return LivePagedListBuilder(userFactory, config).build()
    }

    private val callback = object : PagingCallback {
        override fun onLoading() {
            updateProcessing(true)
        }

        override fun onLoaded() {
            updateProcessing(false)
        }

        override fun onTotalCount(count: Int) {

        }

        override fun onThrowable(throwable: Throwable) {
            JFLog.e(throwable)
        }
    }
}