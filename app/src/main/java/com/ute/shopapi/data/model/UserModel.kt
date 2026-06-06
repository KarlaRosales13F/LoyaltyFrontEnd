package com.ute.shopapi.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("is_staff") val isStaff: Boolean
)

data class UserProfile(
    val telefono: String?,
    val rol: String?,
    val user: User?
)

data class AuthResponse(
    val access: String,
    val refresh: String,
    val user: User? // Sometimes included in login response
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val telefono: String? = null,
    val rol: String = "client"
)
