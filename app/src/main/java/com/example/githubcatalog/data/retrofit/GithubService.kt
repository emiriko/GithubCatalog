package com.example.githubcatalog.data.retrofit

import com.example.githubcatalog.data.response.DetailResponse
import com.example.githubcatalog.data.response.RelationshipResponse
import com.example.githubcatalog.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") username: String,
    ): Call<SearchResponse>
    
    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String,
    ): Call<DetailResponse>
    
    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String,
    ): Call<RelationshipResponse>
    
    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String,
    ): Call<RelationshipResponse>
}