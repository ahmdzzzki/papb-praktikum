package com.example.papbpraktikum

data class GithubProfile(
    val login: String,
    val name: String?,
    val avatar_url: String,
    val followers: Int,
    val following: Int
)
