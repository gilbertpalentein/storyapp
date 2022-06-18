package com.dicoding.storyapp.model

data class UserModel(
    val name: String,
    val userId: String,
    val token: String,
    val lat: Double,
    val lon: Double,
    val isLogin: Boolean
)