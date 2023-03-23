package com.khue.koltincoroutineandflowusecase

import com.khue.koltincoroutineandflowusecase.flow.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


private val handler = CoroutineExceptionHandler { _, exception ->
    println("Caught $exception")
}

private fun main() = runBlocking(handler) {
    createSharedFlow2()
    delay(10000L)
}