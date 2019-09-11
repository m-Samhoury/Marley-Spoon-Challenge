package com.challenge.marleyspoon.models

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */
data class Recipe(
    val id: String? = null,
    val title: String? = null,
    val calories: Double? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val chef: String? = null,
    val tags: List<String>? = null
) {
    companion object {
        const val CONTENT_TYPE_RECIPE = "recipe"
    }
}
