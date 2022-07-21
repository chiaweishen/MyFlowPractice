package com.scw.myflowpractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlowFilterTest {
    @Test
    fun test() = runTest {
        (1..10).asFlow()
            .filter { it % 2 == 0 }
            .filterNot { it % 3 == 0 }
            .collect {
                println(it) // 2 4 8 10
            }

    }
}