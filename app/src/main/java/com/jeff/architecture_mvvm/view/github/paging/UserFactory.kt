package com.jeff.architecture_mvvm.view.github.paging

import androidx.paging.DataSource
import androidx.paging.PagingConfig
import com.jeff.architecture_mvvm.model.api.vo.UserItem

class UserFactory constructor(private val userDataSource: UserDataSource) : DataSource.Factory<String, UserItem>() {
    override fun create(): DataSource<String, UserItem> {
        return userDataSource
    }

    val pagingConfig = PagingConfig(
        // 每頁顯示的數據的大小
        pageSize = 60,

        // 開啟佔位符
        enablePlaceholders = true,

        // 預刷新的距離，距離最後一個 item 多遠時加載數據
        prefetchDistance = 3,

        /**
         * internal const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
         * val initialLoadSize: Int = pageSize * DEFAULT_INITIAL_PAGE_MULTIPLIER
         */
        initialLoadSize = 60,

        /**
         * 一次應在內存中保存的最大數據
         * 這個數字將會觸發，滑動加載更多的數據
         */
        maxSize = 200
    )
}

