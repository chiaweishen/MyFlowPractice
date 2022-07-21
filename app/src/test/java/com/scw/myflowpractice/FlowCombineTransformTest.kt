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
 * https://flowmarbles.com/#combineTransform
 * **/

@OptIn(ExperimentalCoroutinesApi::class)
class FlowCombineTransformTest {
    @Test
    fun testColdFlowCombineTransform() = runTest {
        val flow1 = (1..2).asFlow().onEach { delay(10) }
        val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
        flow1.combineTransform(flow2) { n, a ->
            emit(n)
            emit(a)
        }.collect {
            println(it) // 1 a 2 a 2 b 2 c ->
        }
    }

    @Test
    fun testHotFlowCombineTransform() = runTest {
        val _flow = MutableSharedFlow<Int>(0, 0, BufferOverflow.SUSPEND)
        val flow = _flow.asSharedFlow()

        launch {
            (1..2).onEach {
                delay(20)
                _flow.emit(it)
            }
        }

        val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
        val job2 = launch {
            flow2.combineTransform(flow) { a, n ->
                emit(n)
                emit(a)
            }.collect {
                println(it) //  1 a 1 b 2 b 2 c 1 c 2 c
            }
        }
        advanceUntilIdle()

        val job = launch {
            (1..2).onEach {
                delay(20)
                _flow.emit(it)
            }
        }
        advanceUntilIdle()

        job.cancel()
        job2.cancel()
    }
}