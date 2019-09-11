package com.challenge.marleyspoon.repository.network

import com.challenge.marleyspoon.models.Recipe
import retrofit2.Response
import retrofit2.http.GET


/**
 * This is the service that will include all the network calls for
 * MarleySpoon APIs over contentful
 *
 * @author moustafasamhoury
 * created on Tuesday, 10 September, 2019
 */
interface MarleySpoonService {

    @GET("")
    suspend fun fetchRecipesList(): Response<List<Recipe>>
}