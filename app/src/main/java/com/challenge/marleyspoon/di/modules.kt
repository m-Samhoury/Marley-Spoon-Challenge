package com.challenge.marleyspoon.di

import com.challenge.marleyspoon.BuildConfig
import com.challenge.marleyspoon.features.recipeslist.RecipesListViewModel
import com.challenge.marleyspoon.repository.Repository
import com.challenge.marleyspoon.repository.network.MarleySpoonService
import com.challenge.marleyspoon.repository.network.NetworkLayerUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */
val repositoryModule: Module = module {
    single { NetworkLayerUtils.makeHttpClient(androidContext()) }

    single<MarleySpoonService> { NetworkLayerUtils.makeServiceFactory(get()) }
    single {
        NetworkLayerUtils
            .makeRetrofit(
                BuildConfig.BASE_API_URL,
                get()
            )
    }
    single { NetworkLayerUtils.createCDAClient() }
    single { Repository(get()) }
}
val viewModelsModule = module {
    viewModel {
        RecipesListViewModel(repository = get())
    }
}