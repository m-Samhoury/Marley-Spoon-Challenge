package com.challenge.marleyspoon.features.recipeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.challenge.marleyspoon.base.BaseViewModel
import com.challenge.marleyspoon.models.Recipe
import com.challenge.marleyspoon.repository.Repository
import com.challenge.marleyspoon.repository.network.StateMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */
data class RecipesListState(val stateMonitor: StateMonitor<List<Recipe>> = StateMonitor.Init)

class RecipesListViewModel(
    private val repository: Repository,
    private val recipesListState: RecipesListState = RecipesListState()
) : BaseViewModel() {

    private val _recipesListStateLiveData = MutableLiveData<RecipesListState>()
    val recipesListStateLiveData: LiveData<RecipesListState> = _recipesListStateLiveData

    fun fetchRecipesList(imageWidth: Int? = null) {
        _recipesListStateLiveData.value =
            recipesListState.copy(stateMonitor = StateMonitor.Loading)
        viewModelScope.launch(Dispatchers.Main) {
            val response = repository.fetchRecipesList(imageWidth) {
                _recipesListStateLiveData.value =
                    recipesListState
                        .copy(
                            stateMonitor = StateMonitor
                                .Failed(failed = it)
                        )
            }
            if (response != null) {
                _recipesListStateLiveData.value =
                    recipesListState.copy(stateMonitor = StateMonitor.Loaded(response))
            } else {
                _recipesListStateLiveData.value =
                    recipesListState
                        .copy(
                            stateMonitor = StateMonitor
                                .Failed(failed = Throwable("Unknown Error"))
                        )
            }
        }
    }
}
