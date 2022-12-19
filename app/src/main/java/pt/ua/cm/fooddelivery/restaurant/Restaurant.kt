package pt.ua.cm.fooddelivery.restaurant

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "restaurant")
data class Restaurant(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
)