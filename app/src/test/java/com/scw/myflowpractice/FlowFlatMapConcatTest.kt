@file:OptIn(FlowPreview::class)

package com.scw.myflowpractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * https://waynestalk.com/kotlin-coroutine-flow-tutorial/#flatmapconcat
 * flatMapConcat() 的 transform 會對每一個值回傳一個 flow。
 * 然後，flatMapConcat() 會依序地執行這些 flow，並將這些 flow 裡 emit() 發出的值，依序地全部集合起來變成一個新的 flow。
 * **/

@OptIn(ExperimentalCoroutinesApi::class)
class FlowFlatMapConcatTest {
    @Test
    fun test() = runTest {
        (1..3).asFlow()
            .flatMapConcat {
                flow {
                    emit("${it}a")
                    delay(100)
                    emit("${it}b")
                }
            }
            .collect {
                println(it) // 1a 1b 2a 2b 3a 3b
            }
    }
}