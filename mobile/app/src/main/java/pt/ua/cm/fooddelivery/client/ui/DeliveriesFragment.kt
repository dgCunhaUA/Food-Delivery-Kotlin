package pt.ua.cm.fooddelivery.client.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.client.viewmodel.DeliveriesModelFactory
import pt.ua.cm.fooddelivery.client.viewmodel.DeliveriesViewModel
import pt.ua.cm.fooddelivery.databinding.FragmentDeliveriesBinding
import timber.log.Timber

class DeliveriesFragment : Fragment() {

    private lateinit var binding: FragmentDeliveriesBinding

    private val deliveriesViewModel: DeliveriesViewModel by viewModels {
        DeliveriesModelFactory((activity?.application as DeliveryApplication).orderRepository, (activity?.application as DeliveryApplication).userRepository,)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeliveriesBinding.inflate(layoutInflater)

        setFields()

        return  binding.root
    }

    private fun setFields() {

        val mainActivity = this.activity
        if (mainActivity != null) {
            deliveriesViewModel.ordersResult.observe(viewLifecycleOwner) {
                Timber.i("FRAGMENT OBSERVER RESULT: $it")

                when (it) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }
                    is BaseResponse.Success -> {
                        stopLoading()
                        binding.deliveriesRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity?.applicationContext)
                            adapter = it.data?.let { it1 ->
                                pt.ua.cm.fooddelivery.client.adapter.DeliveriesAdapter(it1)
                            }
                        }
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
            deliveriesViewModel.getAllOrders()

        }
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

}