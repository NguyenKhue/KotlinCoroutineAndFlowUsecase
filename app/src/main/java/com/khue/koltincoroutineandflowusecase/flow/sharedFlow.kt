package com.khue.koltincoroutineandflowusecase.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

//https://medium.com/mobile-app-development-publication/comparing-stateflow-sharedflow-and-callbackflow-2f0d03d48a43

suspend fun createSharedFlow() {
    val sharedFlow = MutableSharedFlow<Int>()

    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.IO) {
            sharedFlow.emit(1)
            delay(100L)
            sharedFlow.emit(2)
            delay(100L)
            sharedFlow.emit(3)
            delay(100L)
            sharedFlow.emit(4)
        }
        launch(Dispatchers.IO) {
            sharedFlow.collect {
                println("sharedFlow: $it")
            }
        }
        launch(Dispatchers.IO) {
            delay(200)
            sharedFlow.collect {
                println("sharedFlow2: $it")
            }
        }
    }
}

suspend fun createSharedFlow1() {
    val sharedFlow = MutableSharedFlow<Int>()

    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.IO) {
            delay(100L)
            sharedFlow.emit(1)
            delay(100L)
            sharedFlow.emit(2)
            delay(100L)
            sharedFlow.emit(3)
            delay(100L)
            sharedFlow.emit(4)
        }
        launch(Dispatchers.IO) {
            sharedFlow.collect {
                println("sharedFlow: $it")
            }
        }
        launch(Dispatchers.IO) {
            delay(200)
            sharedFlow.collect {
                println("sharedFlow2: $it")
            }
        }
    }
}

suspend fun createSharedFlow2() {
    val sharedFlow = MutableSharedFlow<Int>(extraBufferCapacity = 5, replay = 2, onBufferOverflow = BufferOverflow.DROP_LATEST)

    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.IO) {
            sharedFlow.tryEmit(1).also { println("relayCache: ${sharedFlow.replayCache}") }
            sharedFlow.tryEmit(2).also { println("relayCache: ${sharedFlow.replayCache}") }
            sharedFlow.tryEmit(3).also { println("relayCache: ${sharedFlow.replayCache}") }
            sharedFlow.tryEmit(4).also { println("relayCache: ${sharedFlow.replayCache}") }
            sharedFlow.tryEmit(5).also { println("relayCache: ${sharedFlow.replayCache}") }
            delay(500)
            sharedFlow.tryEmit(6).also { println("relayCache: ${sharedFlow.replayCache}") }
            sharedFlow.tryEmit(7).also { println("relayCache: ${sharedFlow.replayCache}") }
            sharedFlow.tryEmit(8).also { println("relayCache: ${sharedFlow.replayCache}") }
            sharedFlow.tryEmit(9).also { println("relayCache: ${sharedFlow.replayCache}") }
        }
        launch(Dispatchers.IO) {
            delay(500L)
            sharedFlow.collect {
                println("sharedFlow: $it")
            }
        }
    }
}