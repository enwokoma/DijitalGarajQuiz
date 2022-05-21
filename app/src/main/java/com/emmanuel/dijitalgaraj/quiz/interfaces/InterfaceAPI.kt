package com.emmanuel.dijitalgaraj.quiz.interfaces

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface InterfaceAPI {
    @PUT(".")
    suspend fun updateRecord(@Body requestBody: RequestBody): Response<ResponseBody>
}