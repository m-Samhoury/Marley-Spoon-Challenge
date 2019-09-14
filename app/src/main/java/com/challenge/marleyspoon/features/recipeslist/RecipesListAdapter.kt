package com.challenge.marleyspoon.features.recipeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.challenge.marleyspoon.R
import com.challenge.marleyspoon.models.Recipe
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_recipes_list.view.*

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */

class RecipesListAdapter(private val onRowClicked: ((View, Int) -> Any)? = null) :
    ListAdapter<Recipe, RecipesListAdapter
    .RecipeViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem == newItem
        }
        private const val MAX_TAGS = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder =
        RecipeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recipes_list, parent, false),
            onRowClicked
        )

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) =
        holder.bind(getItem(position))

    class RecipeViewHolder(
        view: View,
        private val onRowClicked: ((View, Int) -> Any)? = null
    ) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                onRowClicked?.invoke(it, adapterPosition)
            }
        }

        fun bind(item: Recipe) {
            itemView.textViewTitle.text = item.title
            itemView.imageViewThumbnail
                .load(item.thumbnailUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }

            if (item.tags != null) {
                val initialConstraintSet = ConstraintSet()
                initialConstraintSet.clone(itemView.constraintLayoutRoot)
                initialConstraintSet.setVerticalBias(R.id.textViewTitle, 0.0f)
                initialConstraintSet.applyTo(itemView.constraintLayoutRoot)

                for (tag in item.tags.take(MAX_TAGS)) {
                    itemView.chipGroup.addView(Chip(itemView.context).apply {
                        text = tag
                        isClickable = false
                        isCheckable = false
                        chipEndPadding = 0f
                        chipStartPadding = 0f
                    })
                }
            } else {
                val initialConstraintSet = ConstraintSet()
                initialConstraintSet.clone(itemView.constraintLayoutRoot)
                initialConstraintSet.setVerticalBias(R.id.textViewTitle, 0.5f)
                initialConstraintSet.applyTo(itemView.constraintLayoutRoot)
            }
        }
    }
}
