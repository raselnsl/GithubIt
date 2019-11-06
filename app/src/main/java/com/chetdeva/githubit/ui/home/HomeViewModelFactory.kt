package com.chetdeva.githubit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chetdeva.githubit.data.GithubRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val repository: GithubRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}