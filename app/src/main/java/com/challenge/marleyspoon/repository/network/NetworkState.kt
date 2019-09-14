package com.challenge.marleyspoon.repository.network

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 September, 2019
 */

sealed class NetworkState {
    object Loading : NetworkState()
    object Loaded : NetworkState()
    data class Error(val throwable: Throwable, val errorAction:(() -> Any)? = null) : NetworkState()
}
