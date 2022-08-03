package com.scw.myflowpractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HotFlowToColdFlowTest {
    @Test
    fun test() = runTest {
        val flow = MutableSharedFlow<Int>(0, 0, BufferOverflow.SUSPEND)
        val coldFlow: Flow<Int> = flow
        val job = launch {
            coldFlow.collect {
                println(it) // 1 2 3 ... 100
            }
        }

        val job2 = launch {
            (1..100).forEach {
                flow.emit(it)
                delay(10)
            }
        }
        advanceUntilIdle()

        job.cancel()
        job2.cancel()
    }
}