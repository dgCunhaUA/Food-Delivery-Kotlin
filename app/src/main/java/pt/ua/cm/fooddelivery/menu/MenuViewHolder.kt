package pt.ua.cm.fooddelivery.menu

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.MenuItemBinding
import timber.log.Timber

class MenuViewHolder(
    private val context: Context,
    private val binding: MenuItemBinding,
): RecyclerView.ViewHolder(binding.root)
{

    fun bindMenuItem(menu: Menu)
    {
        binding.menuName.text = menu.name

        binding.menuCellContainer.setOnClickListener {
            Timber.i( "CLICKED: $menu")
        }
    }
}