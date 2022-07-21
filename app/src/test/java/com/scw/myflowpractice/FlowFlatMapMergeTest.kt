@file:OptIn(FlowPreview::class)

package com.scw.myflowpractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * https://waynestalk.com/kotlin-coroutine-flow-tutorial/#flatmapmerge
 * 與 flatMapConcat() 不同的是，flatMapMerge() 會同時地執行所有回傳的 flow。
 * 然後，依照 emit() 的順序，將值收集起來放到新的 flow。
 * **/

@OptIn(ExperimentalCoroutinesApi::class)
class FlowFlatMapMergeTest {
    @Test
    fun testFlatMapMergeDoubleEmit() = runTest {
        (1..100).asFlow()
            .flatMapMerge {
                flow {
                    emit("$it: a")
                    emit("$it: b")
                }
            }
            .flatMapMerge {
                flow {
                    emit("$it: c")
                }
            }
            .collect {
                println(it) // 1: a: c 1: b: c 2: a: c ... (random)
            }
    }

    @Test
    fun testFlatMapMergeDelay() = runTest {
        (1..100).asFlow()
            .flatMapMerge {
                flow {
                    emit("$it: a")
                    delay(100)
                    emit("$it: b")
                }
            }
            .flatMapMerge {
                flow {
                    emit("$it: c")
                }
            }
            .collect {
                println(it) // 1: a: c 2: a: c 3: a: c ... (random)
            }
    }
}