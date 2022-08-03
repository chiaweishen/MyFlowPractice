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
 * https://flowmarbles.com/#combine
 * https://waynestalk.com/kotlin-coroutine-flow-tutorial/#combine
 * combine() 會結合多個 flows。與 zip() 很像，但它們從 flow 取值的機制不相同。
 * 只要有一個 flow 有 emit() 值時，它會拿其它 flow 最後的值，即使那些值有被處理過。
 * **/

@OptIn(ExperimentalCoroutinesApi::class)
class FlowCombineTest {
    @Test
    fun testColdFlowCombine() = runTest {
        val flow1 = (1..2).asFlow().onEach { delay(10) }
        val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
        flow1.combine(flow2) { n, a ->
            n.toString() + a
        }.collect {
            println(it) // 1a 2a 2b 2c
        }
    }

    @Test
    fun testHotFlowCombine() = runTest {
        val flow = MutableSharedFlow<Int>(0, 0, BufferOverflow.SUSPEND)

        launch {
            (1..2).onEach {
                delay(20)
                flow.emit(it)
            }
        }

        val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
        val job2 = launch {
            flow2.combine(flow) { a, n ->
                n.toString() + a
            }.collect {
                println(it) // 1a 1b 2b 2c 1c 2c
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