package com.ute.shopapi.data.remote

import com.ute.shopapi.data.model.AuthResponse
import com.ute.shopapi.data.model.LoginRequest
import com.ute.shopapi.data.model.RegisterRequest
import com.ute.shopapi.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthRoutes {
    @POST("api/auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/logout/")
    suspend fun logout(): Response<Unit>

    @GET("api/users/me/")
    suspend fun getMe(): Response<User>
}
