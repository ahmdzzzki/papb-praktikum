package com.example.papbpraktikum

import retrofit2.http.GET

interface ApiService {
    @GET("users/ahmdzzzki")
    suspend fun getGithubProfile(): GithubProfile
}