package com.khue.koltincoroutineandflowusecase

import kotlinx.coroutines.*

fun launchBuilder() = runBlocking {
    // Start a new coroutine using the launch builder
    val job = launch {
        // Coroutine code goes here
        println("Starting coroutine...")
        delay(1000)
        println("Coroutine finished.")
    }
    println("Coroutine started, but not finished yet...")
    job.join() // Wait for the coroutine to finish before exiting the program
}


fun asyncBuilder() = runBlocking {
    // Start two new coroutines using the async builder
    val deferred1 = async {
        delay(1000)
        1
    }
    val deferred2 = async {
        delay(2000)
        2
    }
    // Wait for both coroutines to finish and sum their results
    val sum = deferred1.await() + deferred2.await()
    println("The sum is $sum")
}

fun runBlockingBuilder() = runBlocking {
    // This code runs inside a new coroutine scope created by runBlocking
    println("Starting coroutine...")
    delay(1000)
    println("Coroutine finished.")
}

fun withContextBuilder() = runBlocking {
    // Start a new coroutine and switch to the IO dispatcher
    withContext(Dispatchers.IO) {
        // Coroutine code that runs on the IO dispatcher goes here
        println("Starting coroutine on IO dispatcher...")
        delay(1000)
        println("Coroutine on IO dispatcher finished.")
    }
    // Coroutine code that runs on the main thread goes here
    println("Back on main thread...")
}