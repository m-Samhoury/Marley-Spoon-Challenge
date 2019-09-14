package com.challenge.marleyspoon.repository

import com.challenge.marleyspoon.models.Recipe


/**
 * This interface outlines the functions that needs to be included in the repository
 * implementation class
 *
 * @author moustafasamhoury
 * created on Saturday, 14 September, 2019
 */
interface Repository {

    suspend fun fetchRecipesList(
        imageWidth: Int? = null,
        onError: ((Exception) -> Unit)? = null
    ): List<Recipe>?


}