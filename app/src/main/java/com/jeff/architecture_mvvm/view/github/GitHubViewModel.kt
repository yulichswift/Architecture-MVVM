package com.jeff.architecture_mvvm.view.github

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.paging.PagingChannelData
import com.jeff.architecture_mvvm.view.base.BaseViewModel
import com.jeff.architecture_mvvm.view.github.paging.UserPageRepository
import com.log.JFLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel @Inject internal constructor(
    private val apiRepository: ApiRepository
) : BaseViewModel() {

    private val clearListChannel = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private val requestChannel = MutableSharedFlow<PagingChannelData>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun initLoad() {
        requestChannel.tryEmit(PagingChannelData.Load)
    }

    fun refresh() {
        requestChannel.tryEmit(PagingChannelData.Load)
    }

    fun clear() {
        requestChannel.tryEmit(PagingChannelData.Clear)
        // clearListChannel.tryEmit(Unit)
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

    fun getSimplePageList() = requestChannel.flatMapLatest {
        gitPagingRepository.postData(it)
    }

    /**
     * 多個Channel
     * 宣告一個Channel, 接收清空指令.
     */
    fun getPageList() =
        flowOf(
            clearListChannel.map {
                PagingData.empty()
            },
            requestChannel.flatMapLatest {
                gitPagingRepository.postData(it)
            }
        ).flattenMerge(2)
}