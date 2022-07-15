package com.jeff.architecture_mvvm.view.github.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import com.jeff.architecture_mvvm.paging.PagingChannelData
import com.jeff.architecture_mvvm.paging.PagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserPageRepository(
    private val apiRepository: ApiRepository, private val pagingConfig: PagingConfig, private val pagingCallback: PagingCallback
) : PagingRepository<UserItem>() {
    override fun postData(data: PagingChannelData): Flow<PagingData<UserItem>> {
        return when (data) {
            PagingChannelData.Clear -> flowOf(PagingData.empty())
            PagingChannelData.Load -> Pager(pagingConfig) {
                UserDataSource(
                    apiRepository,
                    pagingCallback,
                    pagingConfig.pageSize
                )
            }.flow
        }
    }
}