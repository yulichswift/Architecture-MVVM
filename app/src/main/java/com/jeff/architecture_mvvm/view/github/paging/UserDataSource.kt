package com.jeff.architecture_mvvm.view.github.paging

import android.webkit.URLUtil
import androidx.paging.PageKeyedDataSource
import com.jeff.architecture_mvvm.callback.PagingCallback
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserDataSource constructor(
    private val viewModelScope: CoroutineScope,
    private val apiRepository: ApiRepository,
    private val pagingCallback: PagingCallback
) : PageKeyedDataSource<String, UserItem>() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, UserItem>) {
        viewModelScope.launch {
            flow {

                val result = apiRepository.fetchUsers(0, 20)
                if (!result.isSuccessful) throw HttpException(result)
                emit(result)
            }
                .flowOn(Dispatchers.IO)
                .onStart {
                    pagingCallback.onLoading()
                }
                .onCompletion {
                    pagingCallback.onLoaded()
                }
                .catch { e ->
                    pagingCallback.onThrowable(e)
                }
                .collect {
                    if (it.isSuccessful) {
                        it.body()?.run {
                            val nextPageUrl =
                                ResponseUtil.parseNextPageUrl(
                                    it.headers()[ResponseUtil.HEADER_KEY_LINK].toString()
                                )
                            callback.onResult(this, null, nextPageUrl)
                        }
                    }
                }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, UserItem>) {
        val url = params.key
        if (URLUtil.isValidUrl(url)) {
            viewModelScope.launch {
                flow {
                    val result = apiRepository.fetchUsers(url)
                    if (!result.isSuccessful) throw HttpException(result)
                    emit(result)
                }
                    .flowOn(Dispatchers.IO)
                    .onStart {
                        pagingCallback.onLoading()
                    }
                    .onCompletion {
                        pagingCallback.onLoaded()
                    }
                    .catch { e ->
                        pagingCallback.onThrowable(e)
                    }
                    .collect {
                        if (it.isSuccessful) {
                            it.body()?.run {
                                val nextUrl =
                                    ResponseUtil.parseNextPageUrl(
                                        it.headers()[ResponseUtil.HEADER_KEY_LINK].toString()
                                    )
                                callback.onResult(this, nextUrl)
                            }
                        }
                    }
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, UserItem>) {
        // Nothing to do here
    }
}