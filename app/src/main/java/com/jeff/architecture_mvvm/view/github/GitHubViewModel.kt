package com.jeff.architecture_mvvm.view.github

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import com.jeff.architecture_mvvm.paging.PagingChannelData
import com.jeff.architecture_mvvm.view.base.BaseViewModel
import com.jeff.architecture_mvvm.view.github.paging.UserPageRepository
import com.log.JFLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.koin.core.inject

class GitHubViewModel : BaseViewModel() {

    private val apiRepository: ApiRepository by inject()

    private val clearListChannel = Channel<Unit>(Channel.CONFLATED)

    private val requestChannel = Channel<PagingChannelData>(Channel.CONFLATED)

    fun initLoad() {
        requestChannel.offer(PagingChannelData.Load)
    }

    fun refresh() {
        requestChannel.offer(PagingChannelData.Load)
    }

    fun clear() {
        requestChannel.offer(PagingChannelData.Clear)
        // clearListChannel.offer(Unit)
    }

    private val pagingConfig = PagingConfig(
        /**
         * initialLoadSize 預設為 pageSize * 3
         */
        initialLoadSize = 30,
        pageSize = 30,
        enablePlaceholders = true,
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

    private val gitPagingRepository by lazy { UserPageRepository(apiRepository, pagingConfig, callback) }

    fun getSimplePageList() = requestChannel.receiveAsFlow().flatMapLatest {
        gitPagingRepository.postData(it)
    }

    /**
     * 多個Channel
     * 宣告一個Channel, 接收清空指令.
     */
    fun getPageList() =
        flowOf(
            clearListChannel.consumeAsFlow().map {
                PagingData.empty<UserItem>()
            },
            requestChannel.consumeAsFlow().flatMapLatest {
                gitPagingRepository.postData(it)
            }
        ).flattenMerge(2)
}