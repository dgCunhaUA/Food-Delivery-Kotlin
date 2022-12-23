package pt.ua.cm.fooddelivery.rider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Index.Order
import pt.ua.cm.fooddelivery.client.adapter.MenuItemClickListener
import pt.ua.cm.fooddelivery.databinding.OrderItemBinding
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse


class OrderAdapter(
    private val orders: List<DeliveriesResponse>,
    private val clickListener: OrderItemClickListener
): RecyclerView.Adapter<OrderViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = OrderItemBinding.inflate(from, parent, false)
        return OrderViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bindOrderItem(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}