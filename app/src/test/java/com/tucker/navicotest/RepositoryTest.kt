package com.tucker.navicotest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tucker.navicotest.fakes.GithubNetworkFake
import com.tucker.navicotest.ui.main.GithubContributorRepository
import com.tucker.navicotest.ui.main.GithubUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Tests for the repository class
 */

class RepositoryTest {

	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@ExperimentalCoroutinesApi
	@Test
	fun `when refresh data succeeds, check LiveData is updated`() = runBlockingTest {
		val list = listOf(GithubUser("mark", 123, "a_url"))
		val githubNetwork = GithubNetworkFake(list)
		val subject = GithubContributorRepository(githubNetwork)
		subject.refreshTopContributorList()
		assertEquals(list, subject.contributorList.value)
	}

	//TODO: add tests using MockWebServer
	//Need to add more tests to do with things like null return values in the JSON, empty responses, that sort of thing

}