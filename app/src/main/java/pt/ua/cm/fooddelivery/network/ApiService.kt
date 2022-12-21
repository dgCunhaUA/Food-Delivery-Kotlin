package pt.ua.cm.fooddelivery.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.network.request.LoginRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://10.0.2.2:3000/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    //.addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()


interface ApiService {

    @POST("/api/client/login")
    fun loginClient(@Body loginRequest: LoginRequest): Call<Client>

    @Multipart
    @POST("/api/client/upload/1")
    fun uploadProfilePicture(@Part photo: MultipartBody.Part): Call<Any>
    //fun uploadProfilePicture(@Part photo: RequestBody, @Part id: RequestBody): Call<Response<Unit>>
    //fun uploadProfilePicture(@Body profilePictureRequest: ProfilePictureRequest): Call<Response<Any>>
    //fun uploadProfilePicture(@Body profilePictureRequest: RequestBody): Call<Response<Any>>

}

object Api {
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}