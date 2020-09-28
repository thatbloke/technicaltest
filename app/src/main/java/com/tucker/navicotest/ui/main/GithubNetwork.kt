package com.tucker.navicotest.ui.main

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.github.com/"

//default parameters to be used for the github API
//These values are for the repo at https://github.com/cli/cli
//it's just a coincidence that the owner and repo values are the same for this one!
const val OWNER = "cli"
const val REPO = "cli"

/**
 * This is the interface for performing the network request to github
 *
 */

private val service: GithubNetwork by lazy {
    //don't need anything fancy here, just use a default okhttp client
    val okhttp = OkHttpClient.Builder().build()

    //github API may return null values if data for a field is not available, we want to be able to have null values
    val gson = GsonBuilder().serializeNulls().create()

    val retrofit = Retrofit.Builder()
        .client(okhttp)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    retrofit.create(GithubNetwork::class.java)
}

fun getGithubNetworkService() = service

interface GithubNetwork {

    /**
    URL from this documentation: https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#list-repository-contributors
    This will get the first page (default is 30) of contributors for the given repository
    The API returns them already sorted in descending order, based on contributions to that repository
    Returns a nullable value - an empty list is of course a valid response
    any other sort of response, we'll get a null list back
    Default parameters are provided - we're using the github cli repository,
    because as of the time of writing this code (Monday afternoon, 28th September 2020) that repo is top of the trending repo list on github
    */
    @GET("/repos/{owner}/{repo}/contributors")
    suspend fun fetchTopContributorList(@Path("owner") owner: String = OWNER, @Path("repo") repo: String = REPO): List<GithubUser>?
}