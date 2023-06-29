package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
class User(
    val id: Int,
    val login: String,
    val password: String,
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val login = varchar("login", 20)
    val password = varchar("password", 20)

    override val primaryKey = PrimaryKey(id)
}
