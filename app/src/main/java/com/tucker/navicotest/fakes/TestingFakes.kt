package com.tucker.navicotest.fakes

import com.tucker.navicotest.ui.main.GithubNetwork
import com.tucker.navicotest.ui.main.GithubUser

/**
 * fake class for helping to test network responses
 * TODO: add optional delay in constructor that's used in the function - this can help with testing the loading spinner too
 *
 * @property list
 */

class GithubNetworkFake(private val list: List<GithubUser>?): GithubNetwork {
    override suspend fun fetchTopContributorList(owner: String, repo: String) = list
}

