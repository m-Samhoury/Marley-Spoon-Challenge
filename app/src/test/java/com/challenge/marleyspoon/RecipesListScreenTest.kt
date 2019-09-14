package com.challenge.marleyspoon

import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.marleyspoon.features.recipeslist.RecipesListViewModel
import com.challenge.marleyspoon.models.Recipe
import com.challenge.marleyspoon.repository.Repository
import com.challenge.marleyspoon.repository.network.StateMonitor
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test

/**
 * Some tests to make sure the RecipeViewModel is emitting the right states for the UI
 * to be then rendered
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
class RecipesListScreenTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    internal lateinit var recipesListViewModel: RecipesListViewModel

    private val currentViewModelState
        get() = LiveDataTestUtil.getValue(
            recipesListViewModel
                .recipesListStateLiveData
        )

    @Test
    fun `loading state is triggered`() =
        coroutinesTestRule.runBlockingTest {
            val repository = object : Repository {
                override suspend fun fetchRecipesList(
                    imageWidth: Int?,
                    onError: ((Exception) -> Unit)?
                ): List<Recipe>? = emptyList()

            }
            recipesListViewModel = RecipesListViewModel(repository)

            coroutinesTestRule.pauseDispatcher()

            recipesListViewModel.fetchRecipesList()

            assertThat(currentViewModelState.stateMonitor is StateMonitor.Loading).isTrue()
            coroutinesTestRule.resumeDispatcher()

        }

    @Test
    fun `successful path with data`() =
        coroutinesTestRule.runBlockingTest {
            val repository = object : Repository {
                override suspend fun fetchRecipesList(
                    imageWidth: Int?,
                    onError: ((Exception) -> Unit)?
                ): List<Recipe>? = listOf(
                    Recipe(id = "1"),
                    Recipe(id = "2"),
                    Recipe(id = "3"),
                    Recipe(id = "4"),
                    Recipe(id = "5")
                )

            }
            recipesListViewModel = RecipesListViewModel(repository)

            coroutinesTestRule.pauseDispatcher()
            recipesListViewModel.fetchRecipesList()

            assertThat(currentViewModelState.stateMonitor is StateMonitor.Loading).isTrue()

            coroutinesTestRule.resumeDispatcher()

            assertThat(currentViewModelState.stateMonitor is StateMonitor.Loaded).isTrue()
            assertThat((currentViewModelState.stateMonitor as StateMonitor.Loaded).result)
                .hasSize(5)
        }

    @Test
    fun `error path`() =
        coroutinesTestRule.runBlockingTest {
            val repository = object : Repository {
                override suspend fun fetchRecipesList(
                    imageWidth: Int?,
                    onError: ((Exception) -> Unit)?
                ): List<Recipe>? {
                    withContext(Dispatchers.Main) {
                        onError?.invoke(NetworkErrorException("No internet"))
                    }
                    return null
                }

            }
            recipesListViewModel = RecipesListViewModel(repository)

            coroutinesTestRule.pauseDispatcher()
            recipesListViewModel.fetchRecipesList()

            assertThat(currentViewModelState.stateMonitor is StateMonitor.Loading).isTrue()

            coroutinesTestRule.resumeDispatcher()

            assertThat(currentViewModelState.stateMonitor is StateMonitor.Failed).isTrue()
            assertThat((currentViewModelState.stateMonitor as StateMonitor.Failed).failed)
                .isInstanceOf(NetworkErrorException::class.java)
        }
}