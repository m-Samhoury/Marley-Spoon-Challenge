package com.challenge.marleyspoon.features.recipedetails

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.challenge.marleyspoon.R
import com.challenge.marleyspoon.base.MarleySpoonFragment
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_recipe_details.*

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */

class RecipeDetailsFragment : MarleySpoonFragment(R.layout.fragment_recipe_details) {

    private val args: RecipeDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateScreen()
    }

    private fun populateScreen() {
        imageViewRecipe
            .load(args.recipe.imageUrl) {
                crossfade(true)
            }
        textViewTitle.text = args.recipe.title

        if (TextUtils.isEmpty(args.recipe.chef)) {
            textViewChefName.visibility = View.GONE
        } else {
            textViewChefName.text = args.recipe.chef
        }

        textViewDescription.text = args.recipe.description
        if (args.recipe.tags != null) {
            for (tag in args.recipe.tags!!) {
                chipGroup.addView(Chip(context).apply {
                    text = tag
                    isClickable = false
                    isCheckable = false
                    chipEndPadding = 0f
                    chipStartPadding = 0f
                })
            }
        } else {
            chipGroup.visibility = View.GONE
        }
    }

    override fun setupViews(rootView: View) {

    }
}
