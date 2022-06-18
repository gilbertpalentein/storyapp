package com.dicoding.storyapp.model

import com.google.gson.annotations.SerializedName

data class UploadPhotoResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
