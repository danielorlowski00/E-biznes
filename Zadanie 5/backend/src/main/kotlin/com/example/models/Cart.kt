package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
class Order(
    val id: Int,
    val itemId: Int,
    val quantity: Int,
    val orderId: Int,
    val userId: Int,
)

object Orders : Table() {
    val id = integer("id").autoIncrement()
    val itemId = integer("itemId")
    val quantity = integer("quantity")
    val orderId = integer("orderId")
    val userId = integer("userId")

    override val primaryKey = PrimaryKey(id)
}
