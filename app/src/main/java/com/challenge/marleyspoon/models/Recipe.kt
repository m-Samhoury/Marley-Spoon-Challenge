package com.challenge.marleyspoon.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */
@Parcelize
data class Recipe(
    val id: String? = null,
    val title: String? = null,
    val calories: Double? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val chef: String? = null,
    val tags: List<String>? = null
) : Parcelable {
    companion object {
        const val CONTENT_TYPE_RECIPE = "recipe"
    }
}
