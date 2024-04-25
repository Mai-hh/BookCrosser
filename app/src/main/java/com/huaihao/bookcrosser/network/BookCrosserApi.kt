package com.huaihao.bookcrosser.network

import android.content.Context
import android.util.Log
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.CommentDTO
import com.huaihao.bookcrosser.model.Drifting
import com.huaihao.bookcrosser.model.DriftingRequest
import com.huaihao.bookcrosser.model.RequestBody
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.model.UserProfile
import com.huaihao.bookcrosser.util.MMKVUtil
import com.huaihao.bookcrosser.util.USER_TOKEN
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.FlowCollector
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
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

    const val TAG = "BookCrosserApi"

    lateinit var bookCrosserApiService: BookCrosserApiService

    const val BASE_URL = "http://192.168.1.208:3001/"
    fun init(context: Context) {

        val httpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val tokenInterceptor = Interceptor { chain ->
            chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    MMKVUtil.getString(USER_TOKEN)
                )
                .build()
                .let {
                    Log.d("BookCrosserApi", "token: ${MMKVUtil.getString(USER_TOKEN)}")
                    Log.d("BookCrosserApi", "Authorization: ${it.headers}")
                    chain.proceed(it)
                }
        }

        val okhttpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
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

        bookCrosserApiService = retrofit.create(BookCrosserApiService::class.java)

    }
}

interface BookCrosserApiService {

    // ================== User ==================
    @POST("/user/register")
    suspend fun register(@Body user: User): Response<TokenResponse>

    @POST("/user/login")
    suspend fun login(@Body user: User): Response<TokenResponse>

    @GET("/user/checkLogin")
    suspend fun checkLogin(): Response<Unit>

    @POST("/user/update")
    suspend fun update(
        @Query("username") username: String,
        @Query("bio") bio: String?,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?
    ): Response<Unit>

    @GET("/user/selectAll")
    suspend fun selectAll(): Response<List<User>>

    @GET("/user/selectById")
    suspend fun selectById(@Query("id") id: Long): User

    @DELETE("/user/deleteById/{id}")
    suspend fun deleteById(@Path("id") id: Long): Boolean

    @DELETE("/user/deleteByEmail/{email}")
    suspend fun deleteByEmail(@Path("email") email: String): Boolean

    // ================== Book ==================

    @GET("/book/selectAll")
    suspend fun selectAllBooks(): Response<List<Book>>

    @POST("/book/shelfABook")
    suspend fun shelfABook(
        @Query("title") title: String,
        @Query("author") author: String,
        @Query("isbn") isbn: String,
        @Query("description") description: String,
        @Query("coverUrl") coverUrl: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<Unit>

    @POST("/book/update")
    suspend fun updateBook(
        @Query("bookId") bookId: Long,
        @Query("title") title: String,
        @Query("author") author: String,
        @Query("description") description: String
    ): Response<Unit>

    @GET("/book/search")
    suspend fun search(
        @Query("title") title: String?,
        @Query("author") author: String?,
        @Query("exact") matchComplete: Boolean
    ): Response<List<Book>>

    @GET("/book/searchByIsbn")
    suspend fun searchByIsbn(@Query("isbn") isbn: String): Response<List<Book>>

    @GET("/user/loadUserProfile")
    suspend fun loadUserProfile(): Response<UserProfile>

    // ================== Drifting ==================
    @GET("/drifting/selectDriftingToMe")
    suspend fun selectDriftingToMe(): Response<List<DriftingRequest>>

    @GET("/drifting/selectMyRequest")
    suspend fun selectMyRequest(): Response<List<DriftingRequest>>

    @POST("/drifting/request")
    suspend fun request(@Query("bookId") bookId: Long): Response<Unit>

    @POST("/drifting/drift")
    suspend fun drift(@Query("requestId") requestId: Long): Response<Unit>

    @POST("/drifting/reject")
    suspend fun rejectDriftingRequest(@Query("requestId") requestId: Long): Response<Unit>

    @POST("/drifting/finish")
    suspend fun driftingFinish(@Query("bookId") bookId: Long): Response<Unit>

    // ================== Comment ==================

    @GET("/comment/selectAll")
    suspend fun selectAllComments(): Response<List<CommentDTO>>

    @GET("/comment/selectMyComments")
    suspend fun selectMyComments(): Response<List<CommentDTO>>

    @POST("/comment/update")
    suspend fun updateComment(
        @Query("id") commentId: Long,
        @Query("content") content: String
    ): Response<Unit>

    @DELETE("/comment/delete")
    suspend fun deleteComment(@Query("id") commentId: Long): Response<Unit>

    @POST("/comment/post")
    suspend fun postComment(
        @Query("bookId") bookId: Long,
        @Query("content") content: String
    ): Response<Unit>

}

