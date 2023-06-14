package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
class Item(
    val id : Int,
    val name: String,
    val categoryName: String,
    val price: Double,
)

object Items: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 20)
    val categoryName = varchar("categoryId", 20)
    val price = double("price")
    override val primaryKey = PrimaryKey(id)
}