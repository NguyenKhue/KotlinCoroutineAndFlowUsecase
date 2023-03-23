package com.khue.koltincoroutineandflowusecase

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//https://medium.com/androiddevelopers/cancellation-in-coroutines-aa6b90163629
//https://kotlinlang.org/docs/cancellation-and-timeouts.html#asynchronous-timeout-and-resources
//https://kt.academy/article/cc-cancellation

// Start Cooperative cancellation
suspend fun coroutineCancellationCase1() {
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

suspend fun coroutineCancellationCase2() {
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

suspend fun coroutineCancellationCase3() {
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

suspend fun coroutineCancellationCase4() {
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
// End Cooperative cancellation

// Start Timeout cancellation
suspend fun coroutineCancellationCase5() {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}

suspend fun coroutineCancellationCase6() {
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

// End Timeout cancellation

// Start Parent-child hierarchy cancellation
suspend fun coroutineCancellationCase7() {
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

suspend fun coroutineCancellationCase8() {
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

// End Parent-child hierarchy cancellation

// Start job cancellation
suspend fun coroutineCancellationCase9() {
    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    coroutineScope.launch {
        delay(1000L)
        println("job running")
    }

    delay(3000L)
    job.cancel()
}

// End job cancellation


// Other case
suspend fun coroutineCancellationCase10() {
    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    coroutineScope.launch {
        val deferred = async {
            delay(1000L)
            throw CancellationException("job2 cancelled")
        }

        deferred.cancel()
        deferred.await()
    }
}

suspend fun coroutineCancellationCase11() {
    withTimeout(2000L) {
        suspendCoroutineCase()
    }
}

suspend fun coroutineCancellationCase12() {
    withTimeout(2000L) {
        suspendCancellableCoroutineCase()
    }
}

private suspend fun suspendCoroutineCase(): Boolean {
    return suspendCoroutine {
        Thread.sleep(3000L)
        it.resume(true).also { println("suspendCoroutineCase resume") }
    }
}

private suspend fun suspendCancellableCoroutineCase(): Boolean {
    return suspendCancellableCoroutine {
        Thread.sleep(3000L)
        it.resume(true)
        it.invokeOnCancellation {
            println("invokeOnCancellation")
        }
    }
}

