package com.jeff.architecture_mvvm.model.api

sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Failure<out T : Any>(val data: T) : ApiResult<T>()
    sealed class Error(val exception: Exception) : ApiResult<Nothing>() {
        class GeneralError(exception: Exception) : Error(exception)
        class RuntimeError(exception: RuntimeException) : Error(exception)
    }

    object SuccessEmpty : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
    object Loaded : ApiResult<Nothing>()
}