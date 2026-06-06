package com.ute.shopapi.data.model

import com.google.gson.annotations.SerializedName

data class PaginatedResponse<T>(
    @SerializedName("data", alternate = ["results"]) val data: List<T>,
    val count: Int,
    val next: String?,
    val previous: String?,
    val page: Int? = null,
    @SerializedName("page_size") val pageSize: Int? = null,
    @SerializedName("total_pages") val totalPages: Int? = null
)
