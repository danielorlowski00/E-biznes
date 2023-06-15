package com.example.plugins

import com.example.models.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )

    val shopService = ShopService(database)

    routing {
        post("/addItem") {
            val item = call.receive<Item>()
            shopService.addItem(item)
            call.respond(item)
        }

        get("/getItemById") {
            val id = call.parameters["id"]!!.toInt()
            val item = shopService.getItemById(id)
            if (item == null) {
                call.respond(GlobalVariables.ITEM_DOES_NOT_EXIST)
            } else {
                call.respond(item)
            }
        }

        get("/getItems") {
            call.respond(shopService.getItems())
        }

        put("updateItem") {
            val item = call.receive<Item>()
            val existingItem = shopService.getItemById(item.id)
            if (existingItem == null) {
                call.respond(GlobalVariables.ITEM_DOES_NOT_EXIST)
            } else {
                shopService.updateItem(item.id, item)
            }
        }

        delete("deleteItem") {
            val id = call.parameters["id"]!!.toInt()
            val existingItem = shopService.getItemById(id)
            if (existingItem == null) {
                call.respond(GlobalVariables.ITEM_DOES_NOT_EXIST)
            } else {
                shopService.deleteItem(id)
            }
        }

        post("/addCategory") {
            val category = call.receive<Category>()
            shopService.addCategory(category)
            call.respond(category)
        }

        get("/getCategoryById") {
            val id = call.parameters["id"]!!.toInt()
            val category = shopService.getCategoryById(id)
            if (category == null) {
                call.respond(GlobalVariables.CATEGORY_DOES_NOT_EXIST)
            } else {
                call.respond(category)
            }
        }

        get("/getCategories") {
            call.respond(shopService.getCategories())
        }

        put("/updateCategory") {
            val category = call.receive<Category>()
            val existingCategory = shopService.getItemById(category.id)
            if (existingCategory == null) {
                call.respond(GlobalVariables.CATEGORY_DOES_NOT_EXIST)
            } else {
                shopService.updateCategory(category.id, category)
            }
        }

        post("/addOrder") {
            val order = call.receive<List<Order>>()
            shopService.addOrder(order)
            call.respond(order)
        }

        get("/getOrderById") {
            val id = call.parameters["id"]!!.toInt()
            val order = shopService.getOrderById(id)
            if (order.isEmpty()) {
                call.respond(GlobalVariables.ORDER_DOES_NOT_EXIST)
            } else {
                call.respond(order)
            }
        }

        get("/getOrders") {
            call.respond(shopService.getOrders())
        }

        delete("/deleteOrder") {
            val id = call.parameters["id"]!!.toInt()
            val order = shopService.getOrderById(id)
            if (order.isEmpty()) {
                call.respond(GlobalVariables.ORDER_DOES_NOT_EXIST)
            } else {
                shopService.deleteOrder(id)
            }
        }

        get("/getPayments") {
            call.respond(shopService.getPayments())
        }

        put("/pay") {
            val paymentToUpdate = call.receive<Payment>()
            val id = paymentToUpdate.id
            val payment = shopService.getPaymentById(id)
            if (payment.isEmpty()) {
                call.respond(GlobalVariables.PAYMENT_DOES_NOT_EXIST)
            } else {
                shopService.updatePayment(id)
                call.respond(shopService.getPaymentById(id))
            }
        }
    }
}