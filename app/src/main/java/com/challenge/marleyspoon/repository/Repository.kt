package com.challenge.marleyspoon.repository

import com.challenge.marleyspoon.repository.network.MarleySpoonService
import retrofit2.Response

/**
 * This repository is our source of truth, we fetch all the data using this repository
 * It may fetch the required data from the network layer or from the local db layer
 *
 * @author moustafasamhoury
 * created on Tuesday, 10 September, 2019
 */
class Repository(private val service: MarleySpoonService) {


    suspend fun fetchRecipesList(
        onError: (Exception) -> Unit
    ) =
        safeApiCall(
            {
                return@safeApiCall service.fetchRecipesList()
            }, {
                onError(it)
            }
        )


    companion object {
        suspend fun <T : Any> safeApiCall(
            call: suspend () -> Response<T>,
            onError: (Exception) -> Unit
        ): T? {

            val result: Result<T> = safeApiResult(call)
            var data: T? = null

            when (result) {
                is Result.Success ->
                    data = result.data
                is Result.Error -> {
                    onError(result.exception)
                }
            }


            return data

        }

        private suspend fun <T : Any> safeApiResult(
            call: suspend () -> Response<T>
        ): Result<T> {
            try {
                val response = call.invoke()
                if (response.isSuccessful) return Result.Success(response.body()!!)

                return Result.Error(java.lang.Exception())
            } catch (exception: Exception) {
                return Result.Error(exception)
            }
        }
    }

}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}