package com.tucker.navicotest

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tucker.navicotest.fakes.GithubNetworkFake
import com.tucker.navicotest.ui.main.GithubContributorRepository
import com.tucker.navicotest.ui.main.GithubUser
import com.tucker.navicotest.ui.main.MainViewModel
import com.tucker.navicotest.ui.main.MainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE, sdk = [Build.VERSION_CODES.P])        //robolectric requires java 9 for SDK targets of API 29+ - I don't have that installed so forcing API 28
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var subject: MainViewModel

    private fun getPopulatedTestList(): List<GithubUser>? {
        return listOf(
            GithubUser("user1", 100, "url1"),
            GithubUser("user2", 50, "url2")
        )
    }

    @Before
    fun setup() {
        val repository = GithubContributorRepository(GithubNetworkFake(getPopulatedTestList()))
        //create a real viewmodel, with a real repository, but with a fake network handler
        subject = MainViewModelFactory(repository).create(MainViewModel::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when load data button clicked, ensure new data is observed`() = runBlockingTest {
        subject.updateContributorList()
        assertEquals(getPopulatedTestList(), subject.contributorList.value)
    }

    @Test
    fun `when toast is shown ensure livedata is null after`() {
        subject.onToastShown()
        assertEquals(null, subject.toast.value)
    }

    //TODO: add delay functionality to fake network handler - that way can test the loading flag too
}