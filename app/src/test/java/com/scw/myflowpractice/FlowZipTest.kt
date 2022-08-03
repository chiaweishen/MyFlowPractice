package com.scw.myflowpractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * https://flowmarbles.com/#zip
 * https://waynestalk.com/kotlin-coroutine-flow-tutorial/#zip
 * zip() 用 transform 將兩個 flows 的值，成對地轉變成一個新的值。
 * 當兩個 flow 的值的數量不同時，當其中一個 flow 的值都處理完後，就會結束。
 * **/

@OptIn(ExperimentalCoroutinesApi::class)
class FlowZipTest {
    @Test
    fun testColdFlowZip() = runTest {
        val flow1 = (1..3).asFlow().onEach { delay(10) }
        val flow2 = flowOf("a", "b", "c", "d").onEach { delay(15) }
        flow1.zip(flow2) { n, a ->
            n.toString() + a
        }.collect {
            println(it) // 1a 2b 3c
        }
    }

    @Test
    fun testHotFlowZip() = runTest {
        val flow = MutableSharedFlow<Int>(0, 0, BufferOverflow.SUSPEND)

        launch {
            (1..2).onEach {
                delay(20)
                flow.emit(it)
            }
        }

        val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
        val job2 = launch {
            flow2.zip(flow) { a, n ->
                n.toString() + a
            }.collect {
                println(it) // 1a 2b 1c
            }
        }
        advanceUntilIdle()

        val job = launch {
            (1..2).onEach {
                delay(20)
                flow.emit(it)
            }
        }
        advanceUntilIdle()

        job.cancel()
        job2.cancel()
    }
}