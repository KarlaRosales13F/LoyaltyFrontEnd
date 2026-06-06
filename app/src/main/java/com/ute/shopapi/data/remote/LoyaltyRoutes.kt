package com.ute.shopapi.data.remote

import com.ute.shopapi.data.model.*
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Url

interface UserRoutes {
    @GET("api/users/me/")
    suspend fun getProfile(): Response<User>
}

interface ComprasRoutes {
    @GET("api/compras/")
    suspend fun getCompras(): Response<PaginatedResponse<Compra>>

    @GET
    suspend fun getComprasPage(@Url url: String): Response<PaginatedResponse<Compra>>

    @POST("api/compras/")
    suspend fun createCompra(@Body request: CompraRequest): Response<Compra>
}

interface PuntosRoutes {
    @GET("api/puntos/{id}/")
    suspend fun getPuntos(@Path("id") userId: Int): Response<PuntosFidelizacion>

    @PUT("api/puntos/acumular/")
    suspend fun acumularPuntos(): Response<PuntosFidelizacion>
}

interface RecompensasRoutes {
    @GET("api/recompensas/")
    suspend fun getRecompensas(): Response<PaginatedResponse<Recompensa>>

    @GET
    suspend fun getRecompensasPage(@Url url: String): Response<PaginatedResponse<Recompensa>>

    @POST("api/recompensas/")
    suspend fun createRecompensa(@Body recompensa: Recompensa): Response<Recompensa>

    @PATCH("api/recompensas/{id}/")
    suspend fun updateRecompensa(@Path("id") id: Int, @Body recompensa: PartialRecompensa): Response<Recompensa>

    @POST("api/recompensas/canjear/")
    suspend fun canjearRecompensa(@Body request: CanjeRequest): Response<Unit>
}

data class PartialRecompensa(
    val stock: Int? = null,
    val estado: Boolean? = null,
)

interface ProductosRoutes {
    @GET("api/productos/")
    suspend fun getProductos(): Response<PaginatedResponse<Producto>>

    @POST("api/productos/")
    suspend fun createProducto(@Body producto: ProductoRequest): Response<Producto>
}

interface DevolucionesRoutes {
    @GET("api/devoluciones/")
    suspend fun getDevoluciones(): Response<PaginatedResponse<Devolucion>>

    @PATCH("api/devoluciones/{id}/")
    suspend fun updateDevolucion(@Path("id") id: Int, @Body request: DevolucionUpdateRequest): Response<Devolucion>
}
