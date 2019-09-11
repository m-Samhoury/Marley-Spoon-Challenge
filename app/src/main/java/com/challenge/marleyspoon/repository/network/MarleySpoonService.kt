package com.challenge.marleyspoon.repository.network

import com.challenge.marleyspoon.BuildConfig
import com.challenge.marleyspoon.models.Recipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * This is the service that will include all the network calls for
 * MarleySpoon APIs over contentful
 *
 * @author moustafasamhoury
 * created on Tuesday, 10 September, 2019
 */
interface MarleySpoonService {

    @GET("entries")
    @Headers("Authorization: ${BuildConfig.API_ACCESS_TOKEN}")
    suspend fun fetchRecipesList(
        @Query("content_type") contentType:String = Recipe.CONTENT_TYPE_RECIPE
    ): Response<List<Recipe>>
}