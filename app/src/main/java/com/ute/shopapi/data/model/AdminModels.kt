package com.ute.shopapi.data.model

import com.google.gson.annotations.SerializedName

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val imagen: Int? = null, // Resource ID for local images
    val stock: Int = 10
)

data class ProductoRequest(
    val nombre: String,
    val descripcion: String?,
    val sku: String?,
    @SerializedName("precio_regular") val precioRegular: Double,
    @SerializedName("precio_descuento") val precioDescuento: Double?,
    val stock: Int,
    @SerializedName("puntos_otorgados") val puntosOtorgados: Int
)

data class Devolucion(
    val id: Int,
    val compra: Int,
    val motivo: String,
    val estado: String,
    @SerializedName("monto_reembolso") val montoReembolso: Double,
    @SerializedName("puntos_recuperados") val puntosRecuperados: Int,
    @SerializedName("fecha_solicitud") val fechaSolicitud: String
)

data class DevolucionUpdateRequest(
    val estado: String
)
