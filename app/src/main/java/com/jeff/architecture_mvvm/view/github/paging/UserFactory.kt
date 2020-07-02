package com.jeff.architecture_mvvm.view.github.paging

import androidx.paging.DataSource
import com.jeff.architecture_mvvm.model.api.vo.UserItem

class UserFactory constructor(private val userDataSource: UserDataSource) : DataSource.Factory<String, UserItem>() {
    override fun create(): DataSource<String, UserItem> {
        return userDataSource
    }
}

