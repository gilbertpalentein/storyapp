package com.dicoding.storyapp.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)