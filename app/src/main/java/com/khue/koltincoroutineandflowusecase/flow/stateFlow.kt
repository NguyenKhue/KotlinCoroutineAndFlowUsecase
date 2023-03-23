package com.khue.koltincoroutineandflowusecase.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

// https://medium.com/geekculture/atomic-updates-with-mutablestateflow-dc0331724405

suspend fun createStateFlow() {
    val stateFlow = MutableStateFlow(false)

    CoroutineScope(Dispatchers.IO).launch {
        launch {
            delay(100L)
            stateFlow.value = true
            delay(100L)
            stateFlow.value = false
            delay(100L)
            stateFlow.value = false
            delay(100L)
            stateFlow.value = true
        }
        launch {
            stateFlow.collect {
                println("stateFlow: $it")
            }
        }
        launch {
            delay(200)
            stateFlow.collect {
                println("stateFlow2: $it")
            }
        }
    }
}

data class State(val x: Int = 0, val y: Int = 0)

suspend fun createStateFlow2() {
    val stateFlow = MutableStateFlow(State())

    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.IO) {
            delay(100L)
            stateFlow.value = stateFlow.value.copy(x = 1)
        }
        launch(Dispatchers.IO) {
            delay(100L)
            stateFlow.value = stateFlow.value.copy(y = 2)
        }
        launch(Dispatchers.IO) {
            stateFlow.collect {
                println("stateFlow: $it")
            }
        }
    }
}

suspend fun createStateFlow3() {
    val stateFlow = MutableStateFlow(State())
    val lock = ReentrantLock()

    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.IO) {
            delay(100L)
            lock.withLock {
                stateFlow.value = stateFlow.value.copy(x = 1)
            }
        }
        launch(Dispatchers.IO) {
            delay(100L)
            lock.withLock {
                stateFlow.value = stateFlow.value.copy(y = 2)
            }
        }
        launch(Dispatchers.IO) {
            stateFlow.collect {
                println("stateFlow: $it")
            }
        }
    }
}

suspend fun createStateFlow4() {
    val stateFlow = MutableStateFlow(State())

    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.IO) {
            delay(100L)
            stateFlow.update { it.copy(x = 1) }
        }
        launch(Dispatchers.IO) {
            delay(100L)
            stateFlow.update { it.copy(y = 2) }
        }
        launch(Dispatchers.IO) {
            stateFlow. collect {
                println("stateFlow: $it")
            }
        }
    }
}