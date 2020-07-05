package com.jeff.architecture_mvvm.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

abstract class PagingRepository<T : Any> {
    abstract fun postData(data: PagingChannelData): Flow<PagingData<T>>
}