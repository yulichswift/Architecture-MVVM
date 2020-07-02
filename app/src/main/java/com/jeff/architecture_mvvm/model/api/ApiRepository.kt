package com.jeff.architecture_mvvm.model.api

class ApiRepository(private val apiService: ApiService) {

    suspend fun fetchUsers(since: Int, perPage: Int) = apiService.fetchUsers(since, perPage)

    suspend fun fetchUsers(url: String) = apiService.fetchUsers(url)

    suspend fun fetchUserDetail(username: String) = apiService.fetchUserDetail(username)

    suspend fun testNoContentApi() = apiService.testNoContentApi()
}