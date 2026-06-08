package com.ute.shopapi.data.remote

import com.ute.shopapi.data.model.*
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Url

interface UserRoutes {
    @GET("users/me/")
    suspend fun getProfile(): Response<User>
}

interface ComprasRoutes {
    @GET("compras/")
    suspend fun getCompras(): Response<PaginatedResponse<Compra>>

    @GET
    suspend fun getComprasPage(@Url url: String): Response<PaginatedResponse<Compra>>

    @POST("compras/")
    suspend fun createCompra(@Body request: CompraRequest): Response<Compra>
}

interface PuntosRoutes {
    @GET("puntos/")
    suspend fun getPuntosList(): Response<PaginatedResponse<PuntosFidelizacion>>

    @GET("puntos/{id}/")
    suspend fun getPuntos(@Path("id") userId: Int): Response<PuntosFidelizacion>

    @PUT("puntos/acumular/")
    suspend fun acumularPuntos(@Body request: AcumularPuntosRequest): Response<PuntosFidelizacion>
}

interface RecompensasRoutes {
    @GET("recompensas/")
    suspend fun getRecompensas(): Response<PaginatedResponse<Recompensa>>

    @GET
    suspend fun getRecompensasPage(@Url url: String): Response<PaginatedResponse<Recompensa>>

    @POST("recompensas/")
    suspend fun createRecompensa(@Body recompensa: Recompensa): Response<Recompensa>

    @PATCH("recompensas/{id}/")
    suspend fun updateRecompensa(@Path("id") id: Int, @Body recompensa: PartialRecompensa): Response<Recompensa>

    @POST("recompensas/canjear/")
    suspend fun canjearRecompensa(@Body request: CanjeRequest): Response<CanjeResponse>
}

data class PartialRecompensa(
    val stock: Int? = null,
    val estado: Boolean? = null,
)

interface ProductosRoutes {
    @GET("productos/")
    suspend fun getProductos(): Response<PaginatedResponse<Producto>>

    @POST("productos/")
    suspend fun createProducto(@Body producto: ProductoRequest): Response<Producto>
}

interface DevolucionesRoutes {
    @GET("devoluciones/")
    suspend fun getDevoluciones(): Response<PaginatedResponse<Devolucion>>

    @PATCH("devoluciones/{id}/")
    suspend fun updateDevolucion(@Path("id") id: Int, @Body request: DevolucionUpdateRequest): Response<Devolucion>
}
