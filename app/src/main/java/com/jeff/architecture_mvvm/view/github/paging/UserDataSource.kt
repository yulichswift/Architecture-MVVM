package com.jeff.architecture_mvvm.view.github.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import retrofit2.HttpException

class UserDataSource constructor(
    private val apiRepository: ApiRepository,
    private val pagingCallback: PagingCallback,
    private val perPageSize: Int
) : PagingSource<String, UserItem>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserItem> {
        try {
            pagingCallback.onLoading()
            val key = params.key
            val result =
                if (key == null) {
                    apiRepository.fetchUsers(0, perPageSize)
                } else {
                    apiRepository.fetchUsers(key)
                }

            if (!result.isSuccessful) {
                val error = HttpException(result)
                pagingCallback.onThrowable(error)
                pagingCallback.onLoaded()
                return LoadResult.Error(error)
            } else {
                result.body()?.also {
                    val nextUrl = ResponseUtil.parseNextPageUrl(result.headers()[ResponseUtil.HEADER_KEY_LINK].toString())
                    pagingCallback.onLoaded()
                    return LoadResult.Page(data = it, prevKey = null, nextKey = nextUrl)
                }
            }

            pagingCallback.onLoaded()
            return LoadResult.Page(data = listOf(), prevKey = null, nextKey = null)
        } catch (e: Exception) {
            pagingCallback.onThrowable(e)
            pagingCallback.onLoaded()
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, UserItem>): String? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let {
                // TODO:
                it.prevKey ?: it.nextKey
            }
        }
    }
}