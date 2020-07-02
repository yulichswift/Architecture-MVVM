package com.jeff.architecture_mvvm.callback

interface PagingCallback {
    fun onLoading()
    fun onLoaded()
    fun onTotalCount(count: Int)
    fun onThrowable(throwable: Throwable)
}