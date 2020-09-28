package com.tucker.navicotest.ui.main

import com.google.gson.annotations.SerializedName

/**
 * data class - info about the user
 * info from here: https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#list-repository-contributors
 * GSON is used by retrofit to convert the JSON into instances of this object
 * we only care about the below data for this app - there are many more things that could be done!
 * @property username   the username of the github user
 * @property numberOfCommits    the number of contributions or commits that the user has made for the given repo
 * @property avatarURL  The URL for their github avatar
 */

data class GithubUser(
    @SerializedName("login")
    val username: String,
    @SerializedName("contributions")
    val numberOfCommits: Int,
    @SerializedName("avatar_url")
    val avatarURL: String?
)