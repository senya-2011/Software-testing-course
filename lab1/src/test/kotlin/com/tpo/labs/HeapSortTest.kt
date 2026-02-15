package com.tpo.labs

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class HeapSortTest: StringSpec({
    "HeapSort should sort an array of integers" {
        checkAll(Arb.int(0, 100)) { x ->
            val arr = intArrayOf(x)
            HeapSort().sort(arr)
            arr shouldBe intArrayOf(x)
        }
    }
})