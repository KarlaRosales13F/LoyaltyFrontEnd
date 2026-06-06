package com.ute.shopapi.data.model

import com.google.gson.annotations.SerializedName

data class Recompensa(
    @SerializedName("id", alternate = ["id_recompensa"]) val id: Int,
    val nombre: String,
    val descripcion: String?,
    @SerializedName("puntos_necesarios") val puntosNecesarios: Int,
    val stock: Int,
    val estado: String
)

data class CanjeRequest(
    @SerializedName("recompensa_id") val recompensaId: Int
)

data class PuntosFidelizacion(
    val id: Int,
    @SerializedName("puntos_acumulados") val puntosAcumulados: Int,
    @SerializedName("puntos_usados") val puntosUsados: Int,
    @SerializedName("nivel_actual") val nivelActual: String?,
    @SerializedName("ultima_actualizacion") val ultimaActualizacion: String?
)
