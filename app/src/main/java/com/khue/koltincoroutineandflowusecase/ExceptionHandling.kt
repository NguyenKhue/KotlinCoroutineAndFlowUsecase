package com.khue.koltincoroutineandflowusecase

import kotlinx.coroutines.*

// https://medium.com/androiddevelopers/exceptions-in-coroutines-ce8da1ec060c

private val handler = CoroutineExceptionHandler { _, exception ->
    println("Caught $exception")
}


fun main() = runBlocking(handler) {
    coroutineException3()
    delay(10000L)
}

private suspend fun coroutineException() {
    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    coroutineScope.launch(handler) {
        launch {
            println("job1: I'm sleeping ...")
            delay(500L)
            throw Exception("job1 error")
        }
        launch {
            delay(600L)
            println("job2: I'm sleeping ...")
        }
    }
}

private suspend fun coroutineException2() {
    val job = SupervisorJob()
    val coroutineScope = CoroutineScope(job + handler)

    coroutineScope.launch {
        launch {
            println("job1: I'm sleeping ...")
            delay(500L)
            throw Exception("job1 error")
        }
        launch {
            delay(600L)
            println("job2: I'm sleeping ...")
        }
    }
}

private suspend fun coroutineException3() {
    val job = SupervisorJob()
    val coroutineScope = CoroutineScope(job + handler)

    coroutineScope.launch() {
        println("job1: I'm sleeping ...")
        delay(500L)
        throw Exception("job1 error")
    }
    coroutineScope.launch() {
        delay(600L)
        println("job2: I'm sleeping ...")
    }
}

private suspend fun coroutineException4() {
    CoroutineScope(handler).launch {
        supervisorScope {
            launch {
                println("job1: I'm sleeping ...")
                delay(500L)
                throw Exception("job1 error")
            }
            launch {
                delay(600L)
                println("job2: I'm sleeping ...")
            }
        }
    }
}