package com.khue.koltincoroutineandflowusecase

import kotlinx.coroutines.*

fun main() = runBlocking {
    // create a coroutine scope and run some code asynchronously
    coroutineScope {
        launch {
            delay(1000)
            println("Hello, world!")
        }
        println("Coroutine started")
    }
    println("Coroutine ended")
}

fun main() = runBlocking {
    // create a supervisor coroutine scope and run some child coroutines
    supervisorScope {
        launch {
            delay(1000)
            println("Hello, world!")
        }
        launch {
            delay(2000)
            throw Exception("Coroutine failed")
        }
    }
    println("Coroutine ended")
}