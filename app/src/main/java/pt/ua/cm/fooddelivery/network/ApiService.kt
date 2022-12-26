package pt.ua.cm.fooddelivery.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.network.request.AcceptOrderRequest
import pt.ua.cm.fooddelivery.network.request.LoginRequest
import pt.ua.cm.fooddelivery.network.request.OrderFinishRequest
import pt.ua.cm.fooddelivery.network.request.UpdateRiderLocationRequest
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import pt.ua.cm.fooddelivery.network.response.OrderFinishResponse
import pt.ua.cm.fooddelivery.rider.entities.Rider
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

    @POST("/api/rider/login")
    fun loginRider(@Body loginRequest: LoginRequest): Call<Rider>

    @Multipart
    @POST("/api/client/upload/1")
    fun uploadProfilePicture(@Part photo: MultipartBody.Part): Call<Any>
    //fun uploadProfilePicture(@Part photo: RequestBody, @Part id: RequestBody): Call<Response<Unit>>
    //fun uploadProfilePicture(@Body profilePictureRequest: ProfilePictureRequest): Call<Response<Any>>
    //fun uploadProfilePicture(@Body profilePictureRequest: RequestBody): Call<Response<Any>>

    @POST("/api/order/create")
    fun createOrder(@Body orderFinishRequest: OrderFinishRequest): Call<OrderFinishResponse>

    @GET("/api/order/client/{clientId}/active")
    fun getAllOrdersById(@Path("clientId") clientId: String) : Call<List<DeliveriesResponse>>

    @GET("/api/order/rider/{riderId}")
    fun getRiderOrdersById(@Path("riderId") riderId: Int) : Call<List<DeliveriesResponse>>

    @PUT("/api/order/accept")
    fun acceptOrder(@Body acceptOrderRequest: AcceptOrderRequest) : Call<DeliveriesResponse>

    @PUT("/api/order/rider/update")
    fun updateRiderLocation(@Body updateRiderLocationRequest: UpdateRiderLocationRequest) : Call<DeliveriesResponse>

    @GET("/api/order/{orderId}/coords")
    fun getRiderLocation(@Path("orderId") orderId: Int): Call<DeliveriesResponse>
}

object Api {
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}