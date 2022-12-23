package pt.ua.cm.fooddelivery

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.viewmodel.LoginViewModel
import pt.ua.cm.fooddelivery.client.viewmodel.UserModelFactory
import pt.ua.cm.fooddelivery.databinding.ActivityLoginBinding
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        UserModelFactory(application as DeliveryApplication, (application as DeliveryApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = ActivityLoginBinding.inflate(layoutInflater)
         setContentView(binding.root)

        loginViewModel.loginResult.observe(this) {
            Timber.i("Login results observer: $it")
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }
                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        loginViewModel.autoLogin()
    }

    private fun startUserActivity() {
        Timber.i("Starting new Activity")
        val intent = Intent(this, ClientActivity::class.java)
        startActivity(intent)
    }


    private fun doLogin() {
        Timber.i("Logging in")
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()
        loginViewModel.loginClient(email = email, pwd = pwd)
    }

    fun doSignup() {

    }

    private fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun processLogin(client: Client?) {
        Timber.i("Process Login for $client")
        showToast("Login Success")
        startUserActivity()
    }

}