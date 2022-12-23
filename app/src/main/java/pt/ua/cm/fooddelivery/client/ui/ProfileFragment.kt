package pt.ua.cm.fooddelivery.client.ui

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.LoginActivity
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.client.viewmodel.ProfileModelFactory
import pt.ua.cm.fooddelivery.client.viewmodel.ProfileViewModel
import pt.ua.cm.fooddelivery.databinding.FragmentProfileBinding
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileModelFactory((activity?.application as DeliveryApplication).userRepository)
    }

    private val api = Api

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
                    val bitmap = data.extras?.get("data") as Bitmap
                    binding.profileImage.setImageBitmap(bitmap)
                    persistImage(bitmap)
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

    private fun persistImage(bitmap: Bitmap) {
        val filesDir: File = requireActivity().filesDir
        Timber.i(filesDir.toString())
        val profilePicture = File(filesDir, "profile_pic.png")
        val os: OutputStream
        try {
            os = FileOutputStream(profilePicture)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: java.lang.Exception) {
            Timber.i("Error writing bitmap $e")
        }

        Timber.i("${profilePicture.isFile}")
        Timber.i(profilePicture.length().toString())
        Timber.i(profilePicture.path)

        //profileViewModel.uploadProfilePicture(profilePicture)
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
        profileViewModel.logout()

        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
}