package com.example.euledit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

data class UploadRequest (
    @Part("appid")
    val appid: RequestBody,
    @Part("original")
    val original: RequestBody,
    @Part("file")
    val file: MultipartBody.Part,
)
