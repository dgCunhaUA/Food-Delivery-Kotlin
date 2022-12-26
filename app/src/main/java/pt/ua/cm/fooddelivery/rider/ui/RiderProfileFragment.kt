package pt.ua.cm.fooddelivery.rider.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.WorkerThread
import androidx.fragment.app.viewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.LoginActivity
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.FragmentRiderProfileBinding
import pt.ua.cm.fooddelivery.rider.entities.Rider
import pt.ua.cm.fooddelivery.rider.viewmodel.RiderProfileModelFactory
import pt.ua.cm.fooddelivery.rider.viewmodel.RiderProfileViewModel
import timber.log.Timber
import java.io.InputStream
import java.net.URL

class RiderProfileFragment : Fragment() {

    lateinit var binding: FragmentRiderProfileBinding

    private val profileViewModel: RiderProfileViewModel by viewModels {
        RiderProfileModelFactory((activity?.application as DeliveryApplication).riderRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Timber.i("onCreateView")
        // Inflate the layout for this fragment
        binding = FragmentRiderProfileBinding.inflate(layoutInflater)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                if (data != null) {
                    val bitmap = data.extras?.get("data") as Bitmap
                    binding.profileImage.setImageBitmap(bitmap)
                    // persistImage(bitmap)
                }
            }
        }

        bindFields()

        binding.profileImage.setOnClickListener {
            Timber.i("Clicked on profile image")
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(intent)
        }

        binding.logoutBtn.setOnClickListener {
            logout()
        }

        return binding.root
    }


    private fun bindFields() {
        profileViewModel.currentRider.observe(viewLifecycleOwner) {
            Timber.i("Current Rider: $it")
            if(it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    loadProfileImage(it)
                }
                Timber.i("evibneirb ${it.name}")
                binding.nameTextField.text = it.name
            }
        }
    }

    @WorkerThread
    private fun loadProfileImage(rider: Rider) {
        try {
            val bitmap =
                BitmapFactory.decodeStream(URL("http://10.0.2.2:3000/api/rider/photo/${rider.id}").content as InputStream)

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
        profileViewModel.logout()

        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
}