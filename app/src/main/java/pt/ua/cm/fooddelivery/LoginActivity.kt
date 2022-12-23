package pt.ua.cm.fooddelivery

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.databinding.ActivityLoginBinding
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.rider.entities.Rider
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        LoginModelFactory(application as DeliveryApplication, (application as DeliveryApplication).userRepository, (application as DeliveryApplication).riderRepository)
    }

    private var loginOption = 0;

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.client_rider_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.client_item -> {
                Toast.makeText(this,"Client Selected",Toast.LENGTH_SHORT).show()
                loginOption = 0
            }
            R.id.rider_item -> {
                Toast.makeText(this,"Rider Selected",Toast.LENGTH_SHORT).show()
                loginOption = 1
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startNewActivity(user: Any?) {
        Timber.i("Starting new Activity")

        try {
            user as Client
            intent = Intent(this, ClientActivity::class.java)
        } catch (_: Exception) {
        }

        try {
            user as Rider
            intent = Intent(this, RiderActivity::class.java)
        } catch (_: Exception) {
        }

        startActivity(intent)
    }


    private fun doLogin() {
        Timber.i("Logging in")
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()

        if(loginOption == 0)
            loginViewModel.loginClient(email = email, pwd = pwd)
        else
            loginViewModel.loginRider(email = email, pwd = pwd)
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

    private fun processLogin(user: Any?) {
        showToast("Login Success")
        startNewActivity(user)
    }

}