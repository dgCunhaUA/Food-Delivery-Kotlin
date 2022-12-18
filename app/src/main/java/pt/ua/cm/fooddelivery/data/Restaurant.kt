package pt.ua.cm.fooddelivery.data

data class Restaurant(
    val id: Long,
    val nameResourceId: Int,
    val menus: ArrayList<Menu>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (id != other.id) return false
        if (nameResourceId != other.nameResourceId) return false
        if (menus != other.menus) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nameResourceId
        result = 31 * result + menus.hashCode()
        return result
    }
}
