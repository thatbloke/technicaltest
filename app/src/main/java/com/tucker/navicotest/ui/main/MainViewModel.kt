package com.tucker.navicotest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GithubContributorRepository) : ViewModel() {

    private val _toast = MutableLiveData<String?>()

    val toast: LiveData<String?>
        get() = _toast

    private val _loading = MutableLiveData(false)

    val loading: LiveData<Boolean>
        get() = _loading

    val contributorList = repository.contributorList


    /**
     * call this from the view to redo the network request
     *
     * @param owner
     * @param repo
     */
    fun updateContributorList(owner: String = OWNER, repo: String = REPO) = launchDataLoad {
        //could optionally add some params here to change the owner and repo but I've set up default params
        repository.refreshTopContributorList(owner, repo)
    }

    /**
     * Used to clear the toast message text - we set up the toast and then call this to reset our value for possible future toasts
     *
     */
    fun onToastShown() {
        _toast.value = null
    }

    /**
     * private function to launch a coroutine in the viewModelScope that will perform the data request and update our state
     *
     * @param block
     */
    private fun launchDataLoad(block: suspend() -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                block()
            } catch (error: ContributorRefreshError) {
                _toast.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }

}