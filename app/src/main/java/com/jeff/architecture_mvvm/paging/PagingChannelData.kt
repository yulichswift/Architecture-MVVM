package com.jeff.architecture_mvvm.paging

sealed class PagingChannelData {
    object Load : PagingChannelData()
    object Clear : PagingChannelData()
}