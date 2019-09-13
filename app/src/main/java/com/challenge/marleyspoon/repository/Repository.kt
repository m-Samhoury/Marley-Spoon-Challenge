package com.challenge.marleyspoon.repository

import com.challenge.marleyspoon.models.Recipe
import com.contentful.java.cda.*
import com.contentful.java.cda.image.ImageOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


/**
 * This repository is our source of truth, we fetch all the data using this repository
 * It may fetch the required data from the network layer or from the local db layer
 *
 * @author moustafasamhoury
 * created on Tuesday, 10 September, 2019
 */
class Repository(private val client: CDAClient) {

    suspend fun fetchRecipesList(
        imageWidth: Int? = null,
        onError: (Exception) -> Unit
    ): List<Recipe>? =
        withContext(Dispatchers.Default) {
            val array = safeApiCDACall({
                val cdaArray = client
                    .fetch<CDAEntry>(CDAEntry::class.java)
                    ?.withContentType(Recipe.CONTENT_TYPE_RECIPE)
                    ?.orderBy(
                        "sys.createdAt"
                    )
                    ?.include(2)
                    ?.all()
                CDAResponse(cdaArray != null, cdaArray)
            }, {
                onError(it)
            })
            return@withContext array?.items()?.map {
                val thumbnailImageOptionsList = ArrayList<ImageOption>()
                thumbnailImageOptionsList.add(ImageOption.https())
                if (imageWidth != null) {
                    thumbnailImageOptionsList.add(ImageOption.widthOf(imageWidth))
                }
                Recipe(
                    id = it.id(),
                    title = (it as LocalizedResource).getField<String>("title"),
                    calories = it.getField<Double>("calories"),
                    description = it.getField<String>("description"),
                    chef = it.getField<CDAEntry>("chef")?.getField<String>("name"),
                    tags = it.getField<ArrayList<CDAEntry>>("tags")
                        ?.map { tag -> tag.getField<String>("name") },
                    thumbnailUrl = it.getField<CDAAsset>("photo")
                        .urlForImageWith(*thumbnailImageOptionsList.toTypedArray()),
                    imageUrl = it.getField<CDAAsset>("photo")
                        .urlForImageWith(ImageOption.https())
                )
            }
        }

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

        private suspend fun <T : CDAResource> safeApiCDAResult(
            call: suspend () -> CDAResponse<T>
        ): Result<T> {
            try {
                val response = call.invoke()
                if (response.isSuccessful) return Result.Success(response.data!!)

                return Result.Error(java.lang.Exception())
            } catch (exception: Exception) {
                return Result.Error(exception)
            }
        }

        private suspend fun <T : CDAResource> safeApiCDACall(
            call: suspend () -> CDAResponse<T>,
            onError: (Exception) -> Unit
        ): T? {
            val result: Result<T> = safeApiCDAResult(call)
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
    }
}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class CDAResponse<out T : CDAResource>(
    val isSuccessful: Boolean,
    val data: T?
)