package com.ute.shopapi.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://rosales-loyalty.uaeftt-ute.site/api/"
    
    private var token: String? = null

    fun setToken(newToken: String?) {
        token = newToken
    }

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        
        // Don't add auth header for login/register if token is missing
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        chain.proceed(requestBuilder.build())
    }.build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authRoutes: AuthRoutes by lazy { instance.create(AuthRoutes::class.java) }
    val userRoutes: UserRoutes by lazy { instance.create(UserRoutes::class.java) }
    val comprasRoutes: ComprasRoutes by lazy { instance.create(ComprasRoutes::class.java) }
    val puntosRoutes: PuntosRoutes by lazy { instance.create(PuntosRoutes::class.java) }
    val recompensasRoutes: RecompensasRoutes by lazy { instance.create(RecompensasRoutes::class.java) }
    val productosRoutes: ProductosRoutes by lazy { instance.create(ProductosRoutes::class.java) }
    val devolucionesRoutes: DevolucionesRoutes by lazy { instance.create(DevolucionesRoutes::class.java) }
}
