package pt.ua.cm.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.ua.cm.fooddelivery.repository.OrderRepository

class DeliveriesViewModel(private val repository: OrderRepository) : ViewModel() {
    // TODO: Implement the ViewModel
}

class DeliveriesModelFactory(private val repository: OrderRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(DeliveriesViewModel::class.java))
            return DeliveriesViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}