package com.example.plugins

import com.example.models.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class ShopService(database: Database) {

    init {
        transaction(database) {
            SchemaUtils.create(Items)
            SchemaUtils.create(Payments)
            SchemaUtils.create(Orders)
            SchemaUtils.create(Users)
        }
    }

    private var minOrderId = 1

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


    suspend fun addItem(item: Item): Int = dbQuery {
        Items.insert {
            it[name] = item.name
            it[categoryName] = item.categoryName
            it[price] = item.price
        }[Items.id]
    }

    suspend fun getItems(): List<Item> {
        return dbQuery {
            Items.selectAll().map { resultRow -> castRowToItem(resultRow) }
        }
    }

    suspend fun getItemById(id: Int): Item? {
        return dbQuery {
            Items.select(Items.id.eq(id)).map { resultRow -> castRowToItem(resultRow) }.singleOrNull()
        }
    }

    suspend fun updateItem(id: Int, item: Item) {
        dbQuery {
            Items.update({ Items.id eq id }) {
                it[name] = item.name
                it[categoryName] = item.categoryName
            }
        }
    }

    suspend fun deleteItem(id: Int) {
        dbQuery {
            Items.deleteWhere { Items.id.eq(id) }
        }
    }

    suspend fun addOrder(orders: List<Order>): Unit = dbQuery {
        var price = 0.0
        val id = orders[0].userId
        for (order in orders) {
            Orders.insert {
                it[itemId] = order.itemId
                it[quantity] = order.quantity
                it[orderId] = minOrderId
                it[userId] = order.userId
            }[Orders.id]
            price += order.quantity.toDouble() * getItemById(order.itemId)!!.price
        }
        val payment = Payment(0, minOrderId, false, price, id)
        minOrderId++
        addPayment(payment)
    }
    suspend fun getOrderById(id: Int): List<Order> {
        return dbQuery {
            Orders.select(Orders.orderId.eq(id)).map { resultRow -> castRowToOrder(resultRow) }
        }
    }

    suspend fun deleteOrder(id: Int) {
        dbQuery {
            Orders.deleteWhere { orderId.eq(id) }
            Payments.deleteWhere { orderId.eq(id) }
        }
    }

    private fun addPayment(payment: Payment) {
        Orders.select(Orders.orderId.eq(payment.id)).map { resultRow -> castRowToOrder(resultRow) }
        Payments.insert {
            it[orderId] = payment.orderId
            it[total] = payment.total
            it[done] = payment.done
            it[userId] = payment.userId
        }[Payments.id]
    }

    suspend fun getPaymentById(id: Int): List<Payment> {
        return dbQuery {
            Payments.select(Payments.id.eq(id)).map { resultRow -> castRowToPayment(resultRow) }
        }
    }

    suspend fun updatePayment(id: Int) {
        dbQuery {
            Payments.update({ Payments.id eq id }) {
                it[done] = true
            }
        }
    }

    suspend fun getPayments(id: Int): List<Payment> {
        return dbQuery {
            Payments.select(Payments.userId eq id).map { resultRow -> castRowToPayment(resultRow) }
        }
    }

    suspend fun getUsers(): List<User> {
        return dbQuery {
            Users.selectAll().map { resultRow -> castRowToUser(resultRow) }
        }
    }
    suspend fun register(user: User): Boolean {
        val usersList: List<User> = dbQuery { Users.select(Users.login eq user.login).map { resultRow -> castRowToUser(resultRow) }}
        return if (usersList.isNotEmpty()) {
            false
        } else {
            dbQuery {
                Users.insert {
                    it[login] = user.login
                    it[password] = user.password
                }[Users.id] }
            true
        }
    }
    suspend fun logIn(user: User): Int {
        val usersList: List<User> = dbQuery { Users.select((Users.login eq user.login) and (Users.password eq user.password)).map { resultRow -> castRowToUser(resultRow) }}
        return if (usersList.isNotEmpty()) {
            usersList[0].id
        } else {
            -1
        }
    }

    suspend fun getUserById(idToGet: Int): User? {
        return dbQuery {
            Users.select( Users.id.eq(idToGet)).map { resultRow -> castRowToUser(resultRow) }.singleOrNull()
        }
    }

    suspend fun deleteUser(idToDelete: Int) {
        dbQuery {
            Users.deleteWhere { id.eq(idToDelete) }
        }
    }

    suspend fun updateUser(id: Int, user: User) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[login] = user.login
                it[password] = user.password
            }
        }
    }

    private fun castRowToItem(row: ResultRow) = Item(
        id = row[Items.id],
        name = row[Items.name],
        categoryName = row[Items.categoryName],
        price = row[Items.price],
    )

    private fun castRowToOrder(row: ResultRow) = Order(
        id = row[Orders.id],
        orderId = row[Orders.orderId],
        itemId = row[Orders.itemId],
        quantity = row[Orders.quantity],
        userId = row[Orders.userId],
    )

    private fun castRowToPayment(row: ResultRow) = Payment(
        id = row[Payments.id],
        orderId = row[Payments.orderId],
        done = row[Payments.done],
        total = row[Payments.total],
        userId = row[Payments.userId],
    )
    private fun castRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        login = row[Users.login],
        password = row[Users.password],
    )
}
