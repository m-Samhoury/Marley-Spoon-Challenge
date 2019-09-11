package com.challenge.marleyspoon.features.recipeslist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.marleyspoon.R
import com.challenge.marleyspoon.base.MarleySpoonFragment
import com.challenge.marleyspoon.models.Recipe
import com.challenge.marleyspoon.repository.network.StateMonitor
import com.challenge.marleyspoon.utils.px
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_recipes_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */

class RecipesListFragment : MarleySpoonFragment(R.layout.fragment_recipes_list) {

    private val recipesListViewModel: RecipesListViewModel by viewModel()

    private val recipesListAdapter by lazy {
        RecipesListAdapter(::onRowItemClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesListViewModel.recipesListStateLiveData.observe(this, Observer {
            handleState(it)
        })

        recipesListViewModel.fetchRecipesList(imageWidth = 80.px())
    }

    override fun setupViews(rootView: View) {
        recyclerViewRecipesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesListAdapter
        }
    }

    private fun handleState(recipesListState: RecipesListState) =
        when (val result = recipesListState.stateMonitor) {
            StateMonitor.Loading -> {
                showLoading(true)
            }
            StateMonitor.Init -> {
                showLoading(false)
            }
            is StateMonitor.Loaded -> {
                showLoading(false)
                populateRecipes(result.result)
            }
            is StateMonitor.Failed -> {
                showError(result.failed, result.action)
            }
        }

    private fun populateRecipes(recipesList: List<Recipe>) =
        recipesListAdapter.submitList(recipesList)

    private fun onRowItemClicked(view: View, position: Int) {

    }

    private fun showError(throwable: Throwable, action: (() -> Any)? = null) {
        val snackBar = Snackbar.make(
            constraintLayoutRoot,
            throwable.message ?: getString(R.string.generic_error_unknown),
            Snackbar.LENGTH_SHORT
        )
        if (action != null) {
            snackBar.setAction(R.string.action_retry) {
                action.invoke()
            }
            snackBar.duration = Snackbar.LENGTH_INDEFINITE
        }
        snackBar.show()
    }

    private fun showLoading(shouldShow: Boolean) {
        if (shouldShow) {
            progressBarLoading.show()
        } else {
            progressBarLoading.hide()
        }
    }
}
