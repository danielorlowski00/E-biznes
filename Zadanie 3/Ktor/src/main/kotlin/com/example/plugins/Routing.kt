package com.example.plugins

import com.slack.api.Slack
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureRouting() {

    val token = System.getenv("SLACK_TOKEN")
    val channelName = System.getenv("CHANNEL_NAME")
    val slack = Slack.getInstance()
    val database = Database(ArrayList())

    routing {
        post("/getCategories") {
            if (database.categories.size > 0) {
                var textToSend = "List of categories: \n"
                var i = 1
                for (category in database.categories) {
                    textToSend += ("$i. " + category.name + "\n")
                    i++
                }
                slack.methods(token).chatPostMessage { it.channel(channelName).text(textToSend)}
                val jsonResponse = Json.encodeToString(database.categories)
                call.respondText { jsonResponse }
            }
            else {
                var textToSend = "Categories do not exist"
                slack.methods(token).chatPostMessage { it.channel(channelName).text(textToSend)}
                call.respondText { textToSend }
            }

        }

        post("/getItemsByCategory") {
            val categoryName = call.receiveParameters()["text"].toString()
            val category = database.categories.find { category -> category.name == categoryName }
            if (category == null) {
                call.respondText { "Category with given name does not exist"}
            }
            else {
                if (category.items.size > 0) {
                    var textToSend = "List of items: \n"
                    var i = 1
                    for (item in category.items) {
                        textToSend += ("$i. " + item.name + " " + item.price + "$\n")
                        i++
                    }
                    slack.methods(token).chatPostMessage { it.channel(channelName).text(textToSend)}
                    val jsonResponse = Json.encodeToString(category.items)
                    call.respondText { jsonResponse }
                }
                else {
                    var textToSend = "Category with given name does not contain items "
                    slack.methods(token).chatPostMessage { it.channel(channelName).text(textToSend)}
                    call.respondText { textToSend }
                }
            }

        }

        post("/addCategory") {
            val categoryName = call.parameters["categoryName"].toString()
            val exists = database.categories.stream().anyMatch{ category -> category.name == categoryName }
            if (exists) {
                call.respondText("Category with given name already exists $categoryName")
            } else {
                val category = Category(categoryName, ArrayList())
                val response = Json.encodeToString(category)
                database.categories.add(category)
                call.respondText { response }
            }
        }

        post("/addItem") {
            val itemName = call.parameters["itemName"].toString()
            val price = call.parameters["price"].toString().toDouble()
            val categoryName = call.parameters["categoryName"].toString()
            val category = database.categories.find { category -> category.name == categoryName }
            if (category == null) {
                call.respondText("Category for given item does not exist $categoryName")
            }
            else {
                val itemExists = category.items.stream().anyMatch{ item -> item.name == itemName }
                if (itemExists) {
                    call.respondText("Item with given name already exists $itemName")
                } else {
                    val item = Item(itemName, price, categoryName)
                    category.items.add(item)
                    val response = Json.encodeToString(item)
                    call.respondText { response }
                }
            }
        }

        delete("/deleteCategory") {
            val categoryName = call.parameters["categoryName"].toString()
            val category = database.categories.find { category -> category.name == categoryName}
            if (category == null) {
                call.respondText("Category with given name does not exist $categoryName")
            }
            else {
                database.categories.remove(category)
                val response = Json.encodeToString(category)
                call.respondText { response }
            }
        }

        delete("/deleteItem") {
            val itemName = call.parameters["itemName"].toString()
            val categoryName = call.parameters["categoryName"].toString()
            val category = database.categories.find { category -> category.name == categoryName}
            if (category == null) {
                call.respondText("Category with given name does not exist $categoryName")
            }
            else {
                val item = category.items.find { item -> item.name == itemName }
                if (item == null) {
                    call.respondText("Item with given name does not exist")
                }
                else {
                    category.items.remove(item)
                    val response = Json.encodeToString(category)
                    call.respondText { response }
                }
            }
        }
    }
}
