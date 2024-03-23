package com.example.githubcatalog.ui.detail_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubcatalog.data.response.DetailResponse
import com.example.githubcatalog.data.response.RelationshipResponse
import com.example.githubcatalog.data.retrofit.APIConfig
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
    
    private val _relationship = MutableLiveData<RelationshipResponse>()
    val relationship: LiveData<RelationshipResponse> = _relationship
    
    private val _isRelationshipLoading = MutableLiveData<Boolean>()
    val isRelationshipLoading: LiveData<Boolean> = _isRelationshipLoading
    
    
    fun getDetailInformation(username: String) {
        _isLoading.value = true
        val client = APIConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
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

        var client: Call<RelationshipResponse>? = null
        when(action) {
            "followers" -> {
                client = APIConfig.getApiService().getUserFollowers(username)
            }
            "following" -> {
                client = APIConfig.getApiService().getUserFollowing(username)
            }
        }
        client?.enqueue(object : Callback<RelationshipResponse> {
            override fun onResponse(call: Call<RelationshipResponse>, response: Response<RelationshipResponse>) {
                if (response.isSuccessful) {
                    _isRelationshipLoading.value = false
                    _relationship.value = response.body()
                } else {
                    _isRelationshipLoading.value = false
                    _snackbarText.value = Event("Failed to get data")
                }
            }

            override fun onFailure(call: Call<RelationshipResponse>, t: Throwable) {
                _isRelationshipLoading.value = false
                _snackbarText.value = Event(t.message ?: "Unknown error")
            }
        })
    }
}