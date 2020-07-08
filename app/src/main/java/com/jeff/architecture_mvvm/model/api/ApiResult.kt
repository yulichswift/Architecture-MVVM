package com.jeff.architecture_mvvm.model.api

sealed class ApiResult<T> {
    companion object {
        fun <T> loading(): ApiResult<T> {
            return Loading()
        }

        fun <T> loaded(): ApiResult<T> {
            return Loaded()
        }

        fun <T> error(throwable: Throwable): ApiResult<T> {
            return Error(throwable)
        }

        fun <T> success(result: T?): ApiResult<T> {
            return when (result) {
                null -> SuccessNoContent()
                else -> Success(result)
            }
        }
    }

    class Loading<T> : ApiResult<T>()
    class Loaded<T> : ApiResult<T>()
    class Error<T>(val exception: Throwable) : ApiResult<T>()
    class SuccessNoContent<T> : ApiResult<T>()
    data class Success<T>(val data: T) : ApiResult<T>()
}