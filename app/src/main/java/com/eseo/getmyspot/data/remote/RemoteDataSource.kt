package com.eseo.getmyspot.data.remote

import com.eseo.getmyspot.data.models.*
import retrofit2.http.*

interface RemoteDataSource {

    @POST("api/create_account")
    @Headers("Content-type: application/json")
    suspend fun createAccount(@Body createAccountBodyParam: CreateAccountBodyParam): CreateAccountResult

    @POST("/api/login")
    @Headers("Content-type: application/json")
    suspend fun connectionToAccount(@Body connectionAccountBodyParam: ConnectionAccountBodyParam): ConnectionAccountResult

    @POST("/api/push_spot")
    @Headers("Content-type: application/json")
    suspend fun addSpot(@Body addSpotBodyParam: AddSpotBodyParam): AddSpotResult

    @POST("/api/profile_picture")
    @Headers("Content-type: application/json")
    suspend fun changeProfilePicture(@Body changeProfilePictureBodyParam: ChangeProfilePictureBodyParam): ChangeProfilePictureResult

    @GET("/api/get_spots")
    @Headers("Content-type: application/json")
    suspend fun getSpots(@Query("pseudo") pseudo: String?, @Query("range_min") range_min: Number, @Query("range_max") range_max: Number): GetSpotsResult
}