package com.khue.koltincoroutineandflowusecase.coroutine_scope

import kotlinx.coroutines.*

fun coroutineScope() = runBlocking {
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

fun supervisorScope() = runBlocking {
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

fun globalScope() {
    GlobalScope.launch {
        println("Hello from GlobalScope!")
    }
    Thread.sleep(1000) // Wait for 1 second to allow the coroutine to execute
}

fun customCoroutineScope() = runBlocking {
    val myScope = CoroutineScope(Dispatchers.Default)
    myScope.launch {
        println("Hello from CoroutineScope!")
    }
}

fun mainScope() = runBlocking {
    val myScope = MainScope()
    myScope.launch {
        println("Hello from MainScope!")
    }
}
