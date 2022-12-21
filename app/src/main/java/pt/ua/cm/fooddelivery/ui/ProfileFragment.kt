package pt.ua.cm.fooddelivery.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.MainActivity
import pt.ua.cm.fooddelivery.databinding.FragmentProfileBinding
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.viewmodel.ProfileModelFactory
import pt.ua.cm.fooddelivery.viewmodel.ProfileViewModel
import timber.log.Timber
import java.io.InputStream
import java.net.URL


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileModelFactory((activity?.application as DeliveryApplication).userRepository)
    }

    private val cameraRequest = 1888

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentProfileBinding.inflate(layoutInflater)

        bindFields()

        binding.logoutBtn.setOnClickListener {
            logout()
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                if (data != null) {
                    binding.profileImage.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
        }

        binding.profileImage.setOnClickListener {
            Timber.i("Clicked on profile image")
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(intent)
        }

        return binding.root
    }

    private fun bindFields() {
        profileViewModel.currentClient.observe(viewLifecycleOwner) {

            if(it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    loadProfileImage(it)
                }
                binding.nameTextField.text = it.name
                binding.addressTextField.text = it.address
            }
        }
    }

    @WorkerThread
    private fun loadProfileImage(client: Client) {
        try {
            val bitmap =
                BitmapFactory.decodeStream(URL("http://10.0.2.2:3000/api/client/photo/${client.id}").content as InputStream)

            Thread(Runnable {
                activity?.runOnUiThread(java.lang.Runnable {
                        binding.profileImage.setImageBitmap(bitmap)
                })
            }).start()
        } catch (ex: Exception) {
            Timber.i("Error ${ex.message}")
        }
    }

    private fun logout() {
        Timber.i("Logging out")
        //profileViewModel.logout()

        /*
        if(view != null) {
            view?.findNavController()
                ?.navigate(R.id.action_restaurant_fragment_to_navigation_login)
        } else {
            Timber.i("Wefwef")
        }

         */
    }
}