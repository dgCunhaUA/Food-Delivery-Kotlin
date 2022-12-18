package pt.ua.cm.fooddelivery.pages.home.details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.data.Menu
import pt.ua.cm.fooddelivery.databinding.MenuItemBinding

class MenuAdapter :
    ListAdapter<Menu, MenuAdapter.MenuViewHolder>(DiffCallback) {

    private lateinit var context: Context

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class MenuViewHolder(private var binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu, context: Context) {
            Log.i("MenuAdapter", "Binding menu")
            binding.menuTitle.text = context.getString(menu.nameResourceId)
        }
    }

    /**
     * Create new views
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        Log.i("MenuAdapter", "onCreateViewHolder")

        context = parent.context
        return MenuViewHolder(
            MenuItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replace the contents of a view
     */
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val current = getItem(position)
        Log.i("MenuAdapter", "onBindViewHolder: $current")
        /*holder.itemView.setOnClickListener {
            //onItemClicked(current)
            Log.i("MenuAdapter", "onClick: $current")
        }*/
        holder.bind(current, context)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return (oldItem.nameResourceId == newItem.nameResourceId )
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }
        }
    }
}

