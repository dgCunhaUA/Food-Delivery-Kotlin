package pt.ua.cm.fooddelivery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.FragmentLoginBinding
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.viewmodel.LoginViewModel
import pt.ua.cm.fooddelivery.viewmodel.UserModelFactory
import timber.log.Timber

class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        UserModelFactory(activity?.application as DeliveryApplication, (activity?.application as DeliveryApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView")
        binding = FragmentLoginBinding.inflate(layoutInflater)

        loginViewModel.loginResult.observe(viewLifecycleOwner) {
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loginViewModel.currentClient.observe(viewLifecycleOwner) {
            Timber.i("Auto login: $it")
            if(it != null) {
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        view?.findNavController()
            ?.navigate(R.id.action_navigation_login_to_navigation_home)
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
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun processLogin(client: Client?) {
        Timber.i("Process Login for $client")
        showToast("Login Success")
        //navigateToHome()
    }

}
