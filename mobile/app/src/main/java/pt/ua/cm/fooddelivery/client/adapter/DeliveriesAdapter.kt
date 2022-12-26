package pt.ua.cm.fooddelivery.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import pt.ua.cm.fooddelivery.databinding.DeliveryItemBinding


class DeliveriesAdapter(
    private val deliveries: List<DeliveriesResponse>,
): RecyclerView.Adapter<DeliveriesViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveriesViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = DeliveryItemBinding.inflate(from, parent, false)
        return DeliveriesViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: DeliveriesViewHolder, position: Int) {
        holder.bindDeliveryItem(deliveries[position])
    }

    override fun getItemCount(): Int = deliveries.size
}