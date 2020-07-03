package com.jeff.architecture_mvvm.view.github

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import com.jeff.architecture_mvvm.view.base.BaseViewModel
import com.jeff.architecture_mvvm.view.github.paging.UserPageRepository
import com.log.JFLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.koin.core.inject

class GitHubViewModel : BaseViewModel() {

    private val apiRepository: ApiRepository by inject()

    private val clearListChannel = Channel<Unit>(Channel.CONFLATED)
    private val requestChannel = Channel<String>(Channel.CONFLATED)

    fun initLoad() {
        requestChannel.offer("initLoad")
    }

    fun refresh() {
        requestChannel.offer("refresh")
    }

    fun clear() {
        clearListChannel.offer(Unit)
    }

    private val pagingConfig = PagingConfig(
        /**
         * 初始化加載數量，默認為 pageSize * 3
         */
        initialLoadSize = 30,

        // 每頁顯示的數據的大小
        pageSize = 30,

        // 開啟佔位符
        enablePlaceholders = true,

        // 預刷新的距離，距離最後一個 item 多遠時加載數據
        prefetchDistance = 3,

        maxSize = 200
    )

    private val callback = object : PagingCallback {
        override fun onLoading() {
            updateProcessing(true)
            JFLog.d("onLoading")
        }

        override fun onLoaded() {
            updateProcessing(false)
            JFLog.d("onLoaded")
        }

        override fun onTotalCount(count: Int) {

        }

        override fun onThrowable(throwable: Throwable) {
            JFLog.e(throwable)
        }
    }

    private val getUserRepository by lazy { UserPageRepository(apiRepository, pagingConfig, callback) }

    fun getPageList() =
        flowOf(
            clearListChannel.consumeAsFlow().map {
                PagingData.empty<UserItem>()
            },
            requestChannel.consumeAsFlow().flatMapLatest {
                getUserRepository.passArgument(it)
            }
        ).flattenMerge(2)
}