package com.chetdeva.githubit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chetdeva.githubit.data.GithubRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val repository: GithubRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchUsersViewModel(repository) as T
    }
}