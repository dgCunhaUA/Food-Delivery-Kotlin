package pt.ua.cm.fooddelivery.rider.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.OrderItemBinding
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import timber.log.Timber


class OrderViewHolder(
    private val binding: OrderItemBinding,
    private val clickListener: OrderItemClickListener
): RecyclerView.ViewHolder(binding.root)
{

    fun bindOrderItem(order: DeliveriesResponse)
    {
        Timber.i(order.toString())
        binding.orderName.text = order.order_status

        if(order.order_status == "Delivering") {
            binding.acceptOrderBtn.visibility = View.GONE

            binding.showMapBtn.setOnClickListener {
                Timber.i("Check Map Delivery")
                clickListener.showOrderMap(order)
            }
        } else {
            binding.acceptOrderBtn.visibility = View.VISIBLE


            binding.acceptOrderBtn.setOnClickListener {
                Timber.i("Accepting Order")
                clickListener.acceptOrder(order)
            }

        }
    }
}