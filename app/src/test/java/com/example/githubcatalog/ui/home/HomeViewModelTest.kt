package com.example.githubcatalog.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.githubcatalog.data.remote.response.SearchResponse
import com.example.githubcatalog.data.remote.retrofit.GithubService
import com.example.githubcatalog.utils.Event
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var apiService: GithubService
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }
    
    @Test
    fun `searchUser fetches data and sets result and snackbarText on success`() {
        val mockResponse = SearchResponse(totalCount = 0, incompleteResults = false, items = emptyList())
        
        val viewModel = HomeViewModel()

        // Mock the API call to return a successful response
        val mockCall = mock(Call::class.java) as Call<SearchResponse>
        Mockito.`when`(apiService.searchUsers("query")).thenReturn(mockCall)
        Mockito.`when`(mockCall.execute()).thenReturn(Response.success(mockResponse))

        // Trigger the search
        viewModel.searchUser("query")

        // Observe LiveData directly (not recommended in production code)
        val observerResult = Observer<SearchResponse> { observedResult ->
            // Assert expected behavior
            MatcherAssert.assertThat(observedResult, CoreMatchers.equalTo(mockResponse))
        }
        viewModel.result.observeForever(observerResult)

        val observerSnackbarText = Observer<Event<String>> { observedSnackbarText ->
            val content = observedSnackbarText.getContentIfNotHandled()
            // Assert expected behavior
            MatcherAssert.assertThat(content, CoreMatchers.equalTo("Success retrieving data"))
        }
        viewModel.snackbarText.observeForever(observerSnackbarText)

        // Remove observers to avoid memory leaks
        viewModel.result.removeObserver(observerResult)
        viewModel.snackbarText.removeObserver(observerSnackbarText)

    }

}