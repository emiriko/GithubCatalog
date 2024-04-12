package com.example.githubcatalog.ui.detail_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubcatalog.data.remote.response.DetailResponse
import com.example.githubcatalog.data.remote.response.ItemsItem
import com.example.githubcatalog.data.remote.retrofit.APIConfig
import com.example.githubcatalog.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProfileViewModel : ViewModel() {

    private val _result = MutableLiveData<DetailResponse>()
    val result: LiveData<DetailResponse> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _relationship = MutableLiveData<List<ItemsItem>>()
    val relationship: LiveData<List<ItemsItem>> = _relationship

    private val _isRelationshipLoading = MutableLiveData<Boolean>()
    val isRelationshipLoading: LiveData<Boolean> = _isRelationshipLoading


    fun getDetailInformation(username: String) {
        _isLoading.value = true
        val client = APIConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _result.value = response.body()
                } else {
                    _isLoading.value = false
                    _snackbarText.value = Event("Failed to get data")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event(t.message ?: "Unknown error")
            }
        })
    }

    fun getRelationship(username: String, action: String) {
        _isRelationshipLoading.value = true

        var client: Call<List<ItemsItem>>? = null

        when (action) {
            "followers" -> {
                client = APIConfig.getApiService().getUserFollowers(username)
            }

            "following" -> {
                client = APIConfig.getApiService().getUserFollowing(username)
            }
        }

        client?.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isRelationshipLoading.value = false
                    _relationship.value = response.body()
                } else {
                    _isRelationshipLoading.value = false
                    _snackbarText.value = Event("Failed to get data")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isRelationshipLoading.value = false
                _snackbarText.value = Event(t.message ?: "Unknown error")
            }
        })
    }
}