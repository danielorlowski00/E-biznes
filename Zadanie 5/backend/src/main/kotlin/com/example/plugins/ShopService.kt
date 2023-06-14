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
            SchemaUtils.create(Categories)
            SchemaUtils.create(Items)
            SchemaUtils.create(Payments)
            SchemaUtils.create(Orders)
        }
    }

    private var minOrderId = 1

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun addCategory(category: Category): Int = dbQuery {
        Categories.insert {
            it[name] = category.name
        }[Categories.id]
    }

    suspend fun getCategories(): List<Category> {
        return dbQuery {
            Categories.selectAll().map { resultRow -> castRowToCategory(resultRow) }
        }
    }

    suspend fun getCategoryById(id: Int): Category? {
        return dbQuery {
            Categories.select(Categories.id.eq(id)).map { resultRow -> castRowToCategory(resultRow) }.singleOrNull()
        }
    }

    suspend fun updateCategory(id: Int, category: Category) {
        dbQuery {
            Categories.update({ Categories.id eq id }) {
                it[name] = category.name
            }
        }
    }

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
        for (order in orders) {
            Orders.insert {
                it[itemId] = order.itemId
                it[quantity] = order.quantity
                it[orderId] = minOrderId
            }[Orders.id]
            price += order.quantity.toDouble() * getItemById(order.itemId)!!.price
        }
        val payment = Payment(0, minOrderId, false, price)
        minOrderId++
        addPayment(payment)
    }

    fun getOrders(): Map<Int, List<Order>> {
        val ordersList: List<Order> = Orders.selectAll().map { resultRow -> castRowToOrder(resultRow) }
        return ordersList.groupBy { it.orderId }
    }

    suspend fun getOrderById(id: Int): List<Order> {
        return dbQuery {
            Orders.select(Orders.orderId.eq(id)).map { resultRow -> castRowToOrder(resultRow) }
        }
    }

    suspend fun deleteOrder(id: Int) {
        dbQuery {
            Orders.deleteWhere { orderId.eq(id) }
        }
    }

    private fun addPayment(payment: Payment) {
        Orders.select(Orders.orderId.eq(payment.id)).map { resultRow -> castRowToOrder(resultRow) }
        Payments.insert {
            it[orderId] = payment.orderId
            it[total] = payment.total
            it[done] = payment.done
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

    suspend fun getPayments(): List<Payment> {
        return dbQuery {
            Payments.selectAll().map { resultRow -> castRowToPayment(resultRow) }
        }
    }

    private fun castRowToCategory(row: ResultRow) = Category(
        id = row[Categories.id],
        name = row[Categories.name],
    )

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
    )

    private fun castRowToPayment(row: ResultRow) = Payment(
        id = row[Payments.id],
        orderId = row[Payments.orderId],
        done = row[Payments.done],
        total = row[Payments.total],
    )
}
