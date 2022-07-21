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
    fun test() = runTest {
        (1..3).asFlow()
            .flatMapMerge {
                flow {
                    emit("${it}a")
                    delay(100)
                    emit("${it}b")
                }
            }
            .collect {
                println(it) // 1a 2a 3a 1b 2b 3b
            }
    }
}