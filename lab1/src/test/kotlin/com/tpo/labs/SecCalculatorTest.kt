package com.tpo.labs

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.math.PI
import kotlin.math.cos

class SecCalculatorTest: StringSpec({

    val eps = 1e-5
    val oracle = { x: Double -> 1.0 / cos(x) }

    "sec(x) should return exact values for standard points (0, ±PI, ±PI/3, ±2PI)" {
        val table = mapOf(
            0.0 to 1.0,
            PI to -1.0,
            2 * PI to 1.0,
            -2 * PI to 1.0,
            -PI to -1.0,
            PI / 3 to 2.0,
            -PI / 3 to 2.0
        )

        table.forEach { (x, expected) ->
            SecCalculator.sec(x) shouldBe (expected plusOrMinus eps)
        }
    }

    "sec(x) should be even: sec(x) == sec(-x)"{
        checkAll(Arb.double(0.0,1.5).filter { !it.isNaN() }) { x ->
            val positive = SecCalculator.sec(x)
            val negative = SecCalculator.sec(-x)

            positive shouldBe (negative plusOrMinus eps)
        }
    }

    "sec(x) should be periodic: sec(x) == sec(x + 2PI·k)" {
        checkAll(
            Arb.double(-1.0,1.0).filter { !it.isNaN() },
            Arb.int(1, 100)
        ) { x, k ->
            val base = SecCalculator.sec(x)
            val shifted = SecCalculator.sec(x + 2*PI*k)

            base shouldBe (shifted plusOrMinus eps)
        }
    }

    "sec(x) should match 1/cos(x)"{
        checkAll(
            Arb.double(-1.5,1.5).filter { !it.isNaN() }
        ) { x ->

            val excepted = oracle(x)
            val actual = SecCalculator.sec(x)

            actual shouldBe (excepted plusOrMinus eps)
        }
    }

    "sec(x) should return Infinity on singularity points (PI/2 + PI·k)"{
        checkAll(
            Arb.int(0, 100)
        ){ k ->
            val actual = SecCalculator.sec(PI / 2 + PI*k)
            actual.isInfinite() shouldBe true
        }
    }

    "sec(x) should handle special floating-point values" {
        SecCalculator.sec(Double.NaN).isNaN() shouldBe true
        SecCalculator.sec(Double.POSITIVE_INFINITY).isNaN() shouldBe true
        SecCalculator.sec(Double.MIN_VALUE) shouldBe (1.0 plusOrMinus eps)
    }
})
