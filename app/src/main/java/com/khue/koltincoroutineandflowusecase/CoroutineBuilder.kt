package com.khue.koltincoroutineandflowusecase

import kotlinx.coroutines.*

fun main() {
    GlobalScope.launch {
        // run some code asynchronously
        delay(1000)
        println("Hello, world!")
    }
    println("Coroutine started")
    Thread.sleep(2000)
}

fun main() {
    CoroutineScope(Dispatchers.Default).launch {
        val deferred = async {
            // run some code asynchronously and return a result
            delay(1000)
            "Hello, world!"
        }
        println("Coroutine started")
        delay(2000)
        println(deferred.await())
    }
}

fun main() = runBlocking<Unit> {
    // run some code synchronously
    println("Hello, world!")
}

fun main() {
    runBlocking {
        // run some code with a specified context
        withContext(Dispatchers.IO) {
            println("Hello, world!")
        }
    }
}

