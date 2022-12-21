package pt.ua.cm.fooddelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File


class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {

    private val api = Api

    val currentClient: LiveData<Client?> = userRepository.currentClient.asLiveData()


    fun uploadProfilePicture(profilePicture: File) {
        CoroutineScope(Dispatchers.IO).launch {

            /*val requestFile: RequestBody = RequestBody.create(
                MediaType.get("image/png"),
                profilePicture
            )
            val photo =
                MultipartBody.Part.createFormData("photo", profilePicture.name, requestFile)
            val id = MultipartBody.Part.createFormData("id", "1")

             */

            /*val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", "1")
                .addFormDataPart("avatarUrl", profilePicture.name, profilePicture.asRequestBody())
                .build()

             */

            /*val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), profilePicture)

            val photo = MultipartBody.Part.createFormData("photo", profilePicture.name, requestFile)

            val id = RequestBody.create(MediaType.parse("multipart/form-data"), "1")

             */

            val photo =
                MultipartBody.Part.createFormData("photo", profilePicture.name, RequestBody.create(
                    MediaType.parse("image/*"), profilePicture))

            //val id = MultipartBody.Part.createFormData("id", "1")


            api.apiService.uploadProfilePicture(photo).enqueue(object:
                Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    //handle error here
                    Timber.i("error $t")
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    //your raw string response
                    Timber.i("response: $response")
                    Timber.i("body: ${response.body()}")
                    Timber.i("code: ${response.code()}")

                }
            })
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.deleteAll()
        }
    }

}

class ProfileModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            return ProfileViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}