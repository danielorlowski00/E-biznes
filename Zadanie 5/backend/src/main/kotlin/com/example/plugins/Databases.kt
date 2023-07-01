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

        put("/updateItem") {
            val item = call.receive<Item>()
            val existingItem = shopService.getItemById(item.id)
            if (existingItem == null) {
                call.respond(GlobalVariables.ITEM_DOES_NOT_EXIST)
            } else {
                shopService.updateItem(item.id, item)
                call.respond(item)
            }
        }

        delete("/deleteItem") {
            val id = call.parameters["id"]!!.toInt()
            val existingItem = shopService.getItemById(id)
            if (existingItem == null) {
                call.respond(GlobalVariables.ITEM_DOES_NOT_EXIST)
            } else {
                shopService.deleteItem(id)
                call.respond(id)
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

        delete("/deleteOrder") {
            val id = call.parameters["id"]!!.toInt()
            val order = shopService.getOrderById(id)
            if (order.isEmpty()) {
                call.respond(GlobalVariables.ORDER_DOES_NOT_EXIST)
            } else {
                shopService.deleteOrder(id)
                call.respond(id)
            }
        }

        get("/getPayments/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val existingUser = shopService.getUserById(id)
            if (existingUser == null) {
                call.respond(GlobalVariables.USER_DOES_NOT_EXIST)
            } else {
                call.respond(shopService.getPayments(id))
            }
        }

        put("/pay") {
            val id = call.parameters["id"]!!.toInt()
            val payment = shopService.getPaymentById(id)
            if (payment.isEmpty()) {
                call.respond(GlobalVariables.PAYMENT_DOES_NOT_EXIST)
            } else {
                shopService.updatePayment(id)
                call.respond(shopService.getPaymentById(id))
            }
        }

        post("/register") {
            val user = call.receive<User>()
            call.respond(shopService.register(user))
        }

        post("/login") {
            val user = call.receive<User>()
            call.respond(shopService.logIn(user))
        }

        get("/getUsers") {
            call.respond(shopService.getUsers())
        }

        get("/getUserById") {
            val id = call.parameters["id"]!!.toInt()
            val existingUser = shopService.getUserById(id)
            if (existingUser == null) {
                call.respond(GlobalVariables.USER_DOES_NOT_EXIST)
            } else {
                call.respond(existingUser)
            }
        }

        delete("/deleteUser") {
            val id = call.parameters["id"]!!.toInt()
            val existingUser = shopService.getUserById(id)
            if (existingUser == null) {
                call.respond(GlobalVariables.USER_DOES_NOT_EXIST)
            } else {
                shopService.deleteUser(id)
                call.respond(id)
            }
        }

        put("/updateUser") {
            val user = call.receive<User>()
            val existingUser = shopService.getUserById(user.id)
            if (existingUser == null) {
                call.respond(GlobalVariables.USER_DOES_NOT_EXIST)
            } else {
                shopService.updateUser(user.id, user)
                call.respond(user)
            }
        }
    }
}