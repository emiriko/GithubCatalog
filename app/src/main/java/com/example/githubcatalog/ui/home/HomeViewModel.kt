package com.example.githubcatalog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubcatalog.data.remote.response.SearchResponse
import com.example.githubcatalog.data.remote.retrofit.APIConfig
import com.example.githubcatalog.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _result = MutableLiveData<SearchResponse>()
    val result: LiveData<SearchResponse> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = APIConfig.getApiService().searchUsers(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _result.value = response.body()
                    _snackbarText.value = Event("Success retrieving data")
                } else {
                    _snackbarText.value = Event("Failed to get data")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event(t.message ?: "Unknown error")
            }
        })
    }
}