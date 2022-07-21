package com.scw.myflowpractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlowEmittersTest {
    @Test
    fun testEmpty() = runTest {
        (1..10).asFlow()
            .transform {
                if (it > 10) {
                    emit(it)
                }
            }
            .onStart { println("onStart") }
            .onEmpty { println("onEmpty") }
            .onCompletion { println("onCompletion") }
            .collect {
                println(it)
            }
    }

    @Test
    fun testCatchException() = runTest {
        (1..10).asFlow()
            .transform {
                if (it > 3) {
                    throw Exception("Value is larger than 3.")
                }
                emit(it)
            }
            .onStart { println("onStart") }
            .onEmpty { println("onEmpty") }
            .onCompletion { println("onCompletion") }
            .catch { e ->
                println("Catch Exception: ${e.message}")
            }
            .collect {
                println(it) // 1 2 3
            }
    }
}