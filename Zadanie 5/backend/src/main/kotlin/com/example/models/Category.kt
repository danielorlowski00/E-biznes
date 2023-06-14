package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
class Category(
    val id: Int,
    val name: String,
)

object Categories : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 20)
    override val primaryKey = PrimaryKey(id)
}
