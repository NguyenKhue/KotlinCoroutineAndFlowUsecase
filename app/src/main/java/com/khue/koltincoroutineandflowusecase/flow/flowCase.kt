package com.khue.koltincoroutineandflowusecase.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.isActive

suspend fun createFlow() {
    val flow1 = flow {
        for (i in 1..3) {
            delay(100) // pretend we are doing something useful here
            println("Emitting $i")
            emit(i) // emit next value
        }
    }

    flow1.collect { value -> println("flow 1: $value") }

    delay(1000L)

    println("----------------------")

    flow1.collect { value -> println("flow 2: $value") }
}

// https://medium.com/mobile-app-development-publication/keep-your-kotlin-flow-alive-and-listening-with-callbackflow-c95e5dd545a
suspend fun createFlow2() {
    val flow2 = callbackFlow {
        for (i in 1..3) {
            delay(100) // pretend we are doing something useful here
            println("Emitting $i")
            send(i) // emit next value and crash if the channel is closed
            //trySend(i) // emit next value and return false if the channel is closed
        }
        awaitClose { println("close") }
    }

    flow2.takeWhile { it <= 2 }.collect { value -> println("flow 1: $value") }
}

suspend fun createFlow3() {
    val flow2 = callbackFlow {
        for (i in 1..3) {
            delay(100) // pretend we are doing something useful here
            println("Emitting $i")
            if(i == 3) {
                close()
            }
            send(i) // emit next value and crash if the channel is closed
        }
        awaitClose { println("close") }
    }

    flow2.collect { value -> println("flow 1: $value") }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun createFlow3_1() {
    val flow2 = callbackFlow {
        for (i in 1..3) {
            delay(100) // pretend we are doing something useful here
            println("Emitting $i")
            if(i == 3) {
                close()
            }
            if(!isClosedForSend) send(i).also { println("send when channel not closed") } // emit next value and crash if the channel is closed
        }
        awaitClose { println("close") }
    }

    flow2.collect { value -> println("flow 1: $value") }
}

suspend fun createFlow4() {
    val flow2 = callbackFlow {
        for (i in 1..3) {
            delay(100) // pretend we are doing something useful here
            println("Emitting $i")
            if(i == 3) {
                close()
            }
            trySend(i).also { println("sent success: ${it.isSuccess}") } // emit next value and return false if the channel is closed
        }
        awaitClose { println("close") }
    }

    flow2.collect { value -> println("flow 1: $value") }
}