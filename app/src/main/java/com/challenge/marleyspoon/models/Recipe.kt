package com.challenge.marleyspoon.models

import android.os.Parcelable
import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.CDAResource
import com.contentful.java.cda.LocalizedResource
import com.contentful.java.cda.image.ImageOption
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

        fun createRecipeFromCDAResource(
            cdaResource: CDAResource,
            imageThumbnailWidth: Int? = null
        ): Recipe {
            val thumbnailImageOptionsList = ArrayList<ImageOption>()
            thumbnailImageOptionsList.add(ImageOption.https())
            if (imageThumbnailWidth != null) {
                thumbnailImageOptionsList.add(ImageOption.widthOf(imageThumbnailWidth))
            }
            return Recipe(
                id = cdaResource.id(),
                title = (cdaResource as LocalizedResource).getField<String>("title"),
                calories = cdaResource.getField<Double>("calories"),
                description = cdaResource.getField<String>("description"),
                chef = cdaResource.getField<CDAEntry>("chef")?.getField<String>("name"),
                tags = cdaResource.getField<ArrayList<CDAEntry>>("tags")
                    ?.map { tag -> tag.getField<String>("name") },
                thumbnailUrl = cdaResource.getField<CDAAsset>("photo")
                    ?.urlForImageWith(*thumbnailImageOptionsList.toTypedArray()),
                imageUrl = cdaResource.getField<CDAAsset>("photo")
                    ?.urlForImageWith(ImageOption.https())
            )
        }
    }
}
