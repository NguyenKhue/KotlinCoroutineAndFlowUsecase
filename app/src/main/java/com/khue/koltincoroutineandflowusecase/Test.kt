package com.khue.koltincoroutineandflowusecase

import kotlin.reflect.KProperty0


fun main() {

    val b = b()
    b.t()
}

class b {
    lateinit var a: String
    fun t() {
        a = "a"
        ::a.isInitialized.runIfTrue {
            println(a)
        }
    }
}

fun Boolean.runIfTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}