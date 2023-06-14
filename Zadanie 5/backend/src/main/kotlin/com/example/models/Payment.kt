package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
class Payment(
    val id: Int,
    val orderId: Int,
    val done: Boolean,
    val total: Double
)

object Payments: Table() {

    val id = integer("id").autoIncrement()
    val orderId = integer("orderId")
    val total = double("total")
    val done = bool("done")
    override val primaryKey = PrimaryKey(id)
}



