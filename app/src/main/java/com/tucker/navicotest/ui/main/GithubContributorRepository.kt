package com.tucker.navicotest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Repository class for our data
 * Right now this does not do any local caching - that could be added at a later date, perhaps with a configurable timeout
 * So it will fetch the data from the network and then store the most recently fetched data
 *
 * @property githubNetwork The network service class, from Retrofit
 */

class GithubContributorRepository(private val githubNetwork: GithubNetwork) {

    //This is what the viewmodel sees
    val contributorList: LiveData<List<GithubUser>?>
        get() = _contributorList

    //start with null value - will provide button to initialise the request
    //no caching currently provided, will have to request data on every app launch for now
    private val _contributorList: MutableLiveData<List<GithubUser>?> = MutableLiveData(null)

    /**
     * suspend function launched in a coroutine to perform the actual network request
     *
     * @param owner
     * @param repo
     */
    suspend fun refreshTopContributorList(owner: String = OWNER, repo: String = REPO) {
        try {
            val newList = githubNetwork.fetchTopContributorList(owner, repo)
            _contributorList.value = newList
        } catch (error: Throwable) {
            //ideally this string would be a resource but hardcoding for now
            //could add my own Application class and then get app context from that
            val message = "Unable to obtain contributor list"
            //Log.e("GHConRepo", message, error)
            throw ContributorRefreshError(message, error)
        }
    }
}

/**
 * Thrown when there was an error fetching the contributor list
 *
 * @param message
 * @param cause
 */
class ContributorRefreshError(message: String, cause: Throwable): Throwable(message, cause)