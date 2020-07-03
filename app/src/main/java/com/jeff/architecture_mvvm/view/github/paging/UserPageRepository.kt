package com.jeff.architecture_mvvm.view.github.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import com.log.JFLog
import kotlinx.coroutines.flow.Flow

class UserPageRepository(
    private val apiRepository: ApiRepository,
    private val pagingConfig: PagingConfig,
    private val pagingCallback: PagingCallback
) {

    fun passArgument(argument: String): Flow<PagingData<UserItem>> {
        JFLog.d("Pass: $argument")
        return Pager(pagingConfig) { UserDataSource(apiRepository, pagingCallback, pagingConfig.pageSize) }.flow
    }
}