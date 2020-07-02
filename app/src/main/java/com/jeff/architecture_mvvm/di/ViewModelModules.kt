package com.jeff.architecture_mvvm.di

import com.jeff.architecture_mvvm.view.github.GitHubViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { GitHubViewModel() }
}