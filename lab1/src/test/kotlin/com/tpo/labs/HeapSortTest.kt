package com.tpo.labs

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class HeapSortTest: StringSpec({

    beforeTest {
        HeapSort.trace.clear()
    }

    fun normalize(trace: List<String>): List<String> {
        return trace.map {
            when {
                it.startsWith("Т10") -> "T10"
                it.startsWith("Т11") -> "T11"
                it.startsWith("Т1") -> "T1"
                it.startsWith("Т2") -> "T2"
                it.startsWith("Т3") -> "T3"
                it.startsWith("Т4") -> "T4"
                it.startsWith("Т5") -> "T5"
                it.startsWith("Т6") -> "T6"
                it.startsWith("Т7") -> "T7"
                it.startsWith("Т8") -> "T8"
                it.startsWith("Т9") -> "T9"
                else -> "UNKNOWN"
            }
        }
    }

    "Full trace test for [10, 7, 19, 13]" {

        val arr = intArrayOf(10, 7, 19, 13)

        HeapSort.sort(arr)

        arr.toList() shouldBe listOf(7, 10, 13, 19)

        val actual = normalize(HeapSort.trace)

        val expected = listOf(
            "T1", "T2", "T3", "T5", "T6", "T7",
            "T2", "T3", "T5", "T4", "T5", "T6", "T7",
            "T8", "T9", "T10",
            "T3", "T5", "T4", "T6", "T7",
            "T9", "T10",
            "T3", "T9", "T10", "T11"
        )

        actual shouldBe expected
    }

    "Trace for single element array [5]" {

        val arr = intArrayOf(5)
        HeapSort.sort(arr)

        HeapSort.trace shouldBe listOf(
            "Т1: Старт сортировки",
            "Т8: Начало исключения элементов",
            "Т11: Конец сортировки"
        )
    }



    "HeapSort should sort array correctly" {
        checkAll(
            Arb.list(Arb.int(0, 100), 0..30))
        { list ->
            val arr = list.toIntArray()
            HeapSort.sort(arr)
            arr.toList() shouldBe list.sorted()
        }
    }
})