package com.ute.shopapi.data.model

import com.google.gson.annotations.SerializedName

data class Compra(
    val id: Int,
    val total: Double,
    @SerializedName("metodo_pago") val metodoPago: String,
    @SerializedName("fecha_compra") val fechaCompra: String,
    val estado: String? = "en local", 
    val direccion: String? = null,
    val items: List<CompraItem>?
)

data class CompraItem(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val cantidad: Int,
    @SerializedName("precio_unitario") val precioUnitario: Double
)

data class CompraRequest(
    @SerializedName("metodo_pago") val metodoPago: String,
    val total: Double,
    val direccion: String? = null,
    val estado: String = "en local"
)
