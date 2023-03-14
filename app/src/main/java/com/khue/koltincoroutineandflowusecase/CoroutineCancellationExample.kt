package com.khue.koltincoroutineandflowusecase

import kotlinx.coroutines.*

val handler = CoroutineExceptionHandler { _, exception ->
    println("Caught $exception")
}

private fun main() = runBlocking(handler) {
    coroutineCancellationCase8()
    delay(10000L)
}

private suspend fun coroutineCancellationCase1() {
    val startTime = System.currentTimeMillis()
    val job = GlobalScope.launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // computation loop, just wastes CPU
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

private suspend fun coroutineCancellationCase2() {
    val job = GlobalScope.launch(Dispatchers.Default) {
        repeat(5) { i ->
            try {
                // print a message twice a second
                println("job: I'm sleeping $i ...")
                delay(500)
            } catch (e: Exception) {
                // log the exception
                println(e)
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

private suspend fun coroutineCancellationCase3() {
    val startTime = System.currentTimeMillis()
    val job = GlobalScope.launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // cancellable computation loop
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

private suspend fun coroutineCancellationCase4() {
    val job = GlobalScope.launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("job: I'm running finally")
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

private suspend fun coroutineCancellationCase5() {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}

private suspend fun coroutineCancellationCase6() {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i - ${System.currentTimeMillis()}")
            delay(500L)
        }
        // will get cancelled before it produces this result
        "Done"
    }
    println("Result is $result")
}

private suspend fun coroutineCancellationCase7() {
    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    coroutineScope.launch() {
        launch {
            repeat(10) { i ->
                println("job1: I'm sleeping $i ...")
                delay(500L)
                throw CancellationException("job2 cancelled")
            }
        }
        launch {
            repeat(10) { i ->
                println("job2: I'm sleeping $i ...")
                delay(400L)
                throw CancellationException("job2 cancelled")
            }
        }
    }

    delay(600L) // delay a bit
    println("Result is ${coroutineScope.isActive}")
    //job.cancel()
}

private suspend fun coroutineCancellationCase8() {
    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    coroutineScope.launch {
        launch {
            try {
                println("job1: I'm sleeping")
                delay(500L)
            } catch (e: Exception) {
                println("job1: exception $e")
            }

            println("job1: I'm still running")
        }
    }

    delay(300L)
    job.cancel()
}

// vd suspendCoroutine vs suspendCancellableCoroutine
// nói về cancel and join
// async await