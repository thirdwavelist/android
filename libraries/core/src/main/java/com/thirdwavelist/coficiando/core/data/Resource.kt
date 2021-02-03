package com.thirdwavelist.coficiando.core.data

enum class Status { SUCCESS, ERROR, LOADING }

// TODO: Rewrite this as sealed class representing view state
data class Resource<out T>(
        val status: Status,
        val data: T? = null,
        val error: Throwable? = null) {

    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data = data)
        fun <T> error(error: Throwable): Resource<T> = Resource(Status.ERROR, error = error)
        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data = data)
    }
}