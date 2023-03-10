package pt.ua.cm.fooddelivery.rider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.FragmentRiderHomeBinding
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import pt.ua.cm.fooddelivery.rider.adapter.OrderAdapter
import pt.ua.cm.fooddelivery.rider.adapter.OrderItemClickListener
import pt.ua.cm.fooddelivery.rider.viewmodel.RiderHomeModelFactory
import pt.ua.cm.fooddelivery.rider.viewmodel.RiderHomeViewModel
import timber.log.Timber

class RiderHomeFragment : Fragment(), OrderItemClickListener {

    private lateinit var binding: FragmentRiderHomeBinding

    private val homeViewModel: RiderHomeViewModel by viewModels {
        RiderHomeModelFactory(
            (activity?.application as DeliveryApplication).riderRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Timber.i("onCreateView")

        binding = FragmentRiderHomeBinding.inflate(layoutInflater)



        observerOrderResults()


        return binding.root
    }


    private fun observerOrderResults() {
        homeViewModel.orderResult.observe(viewLifecycleOwner) {
            Timber.i("Cart results observer: $it")
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    stopLoading()
                    Timber.i("sucess: ${it.data}")
                    setRecyclerView(it.data)
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
        homeViewModel.orderAcceptedResult.observe(viewLifecycleOwner) {
            Timber.i("Cart results observer: $it")
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    stopLoading()
                    Timber.i("success: ${it.data}")

                    val list: List<DeliveriesResponse> = if(it.data != null)
                        listOf(it.data)
                    else
                        listOf()

                    Timber.i(list.toString())
                    setRecyclerView(list)
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
        homeViewModel.getOrders()
    }

    private fun setRecyclerView(orders: List<DeliveriesResponse>?) {
        binding.riderOrdersRecyclerView.apply {
            if(orders != null) {
                layoutManager = LinearLayoutManager(activity?.applicationContext)
                adapter =
                    OrderAdapter(orders, this@RiderHomeFragment)
            }
        }
    }

    override fun acceptOrder(order: DeliveriesResponse) {
        homeViewModel.acceptOrder(order)
    }

    override fun showOrderMap(order: DeliveriesResponse) {
        val bundle = bundleOf("clientAddress" to order.client_address, "orderId" to order.id)
        view?.findNavController()
            ?.navigate(R.id.action_rider_navigation_home_to_riderMapFragment, bundle)
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