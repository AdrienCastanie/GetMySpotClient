package com.eseo.getmyspot.data.remote

import com.eseo.getmyspot.data.models.ConnectionAccountBodyParam
import com.eseo.getmyspot.data.models.ConnectionAccountResult
import com.eseo.getmyspot.data.models.CreateAccountBodyParam
import com.eseo.getmyspot.data.models.CreateAccountResult
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RemoteDataSource {

    @POST("api/create_account")
    @Headers("Content-type: application/json")
    suspend fun createAccount(@Body createAccountBodyParam: CreateAccountBodyParam): CreateAccountResult

    @POST("/api/login")
    @Headers("Content-type: application/json")
    suspend fun connectionToAccount(@Body connectionAccountBodyParam: ConnectionAccountBodyParam): ConnectionAccountResult
}