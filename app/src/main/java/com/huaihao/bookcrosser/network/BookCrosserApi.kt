package com.huaihao.bookcrosser.network

import android.content.Context
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.util.MMKVUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object BookCrosserApi {
    lateinit var retrofitService: BookCrosserApiService

    const val BASE_URL = "http://localhost:3001"
    fun init(context: Context) {

        val httpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val requestInterceptor = Interceptor { chain ->
            chain.request().newBuilder()
                .build()
                .let { chain.proceed(it) }
        }

        val tokenInterceptor = Interceptor { chain ->
            chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    MMKVUtil.getString("USER_TOKEN")
                )
                .build()
                .let { chain.proceed(it) }
        }

        val okhttpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .addInterceptor(requestInterceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okhttpClient)
            .baseUrl(BASE_URL)
            .build()

        retrofitService = retrofit.create(BookCrosserApiService::class.java)

    }
}

interface BookCrosserApiService {
    @POST("/user/save")
    suspend fun save(@Body user: User): Boolean

    @POST("/user/register")
    suspend fun register(@Body user: User): Response<Unit>

    @POST("/user/login")
    suspend fun login(@Body user: User): Response<Unit>

    @POST("/user/update")
    suspend fun update(@Body user: User): Boolean

    @GET("/user/selectAll")
    suspend fun selectAll(): List<User>

    @GET("/user/selectById")
    suspend fun selectById(@Query("id") id: Long): User

    @DELETE("/user/deleteById/{id}")
    suspend fun deleteById(@Path("id") id: Long): Boolean

    @DELETE("/user/deleteByEmail/{email}")
    suspend fun deleteByEmail(@Path("email") email: String): Boolean
}

enum class ApiStatus {
    SUCCESSFUL,
    LOADING,
    ERROR
}