package com.scw.myflowpractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ColdFlowToHotFlowTest {
    @Test
    fun test() = runTest {
        val hotFlow: StateFlow<Int> = (1..3).asFlow()
            .stateIn(this, SharingStarted.Lazily, 0)

        val job = launch {
            hotFlow.collect {
                println(it) // 0 3
            }
        }
        advanceUntilIdle()
        job.cancel()
    }
}