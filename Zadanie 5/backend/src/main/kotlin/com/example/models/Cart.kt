package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
class Order(
    val id: Int,
    val itemId: Int,
    val quantity: Int,
    val orderId: Int,
)

object Orders: Table() {
    val id = integer("id").autoIncrement()
    val itemId = integer("itemId")
    val quantity = integer("quantity")
    val orderId = integer("orderId")

    override val primaryKey = PrimaryKey(id)
}



