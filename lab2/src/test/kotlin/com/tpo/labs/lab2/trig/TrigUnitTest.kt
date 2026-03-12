package com.tpo.labs.lab2.trig

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.checkAll
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class TrigUnitTest : StringSpec({
    val eps = 1e-5

    val sinFn = SinFunction()
    val cosFn = CosFunction()

    "sin(x) should match kotlin sin(x) (range-limited)" {
        checkAll(Arb.double(-1.5, 1.5).filter { it.isFinite() }) { x ->
            sinFn.calculate(x) shouldBe (sin(x) plusOrMinus eps)
        }
    }

    "cos(x) should match kotlin cos(x) (range-limited)" {
        checkAll(Arb.double(-1.5, 1.5).filter { it.isFinite() }) { x ->
            cosFn.calculate(x) shouldBe (cos(x) plusOrMinus eps)
        }
    }

    "sin and cos should satisfy sin(0)=0, cos(0)=1" {
        sinFn.calculate(0.0) shouldBe (0.0 plusOrMinus eps)
        cosFn.calculate(0.0) shouldBe (1.0 plusOrMinus eps)
    }

    "cos should be even and sin should be odd (range-limited)" {
        checkAll(Arb.double(-1.0, 1.0).filter { it.isFinite() }) { x ->
            cosFn.calculate(x) shouldBe (cosFn.calculate(-x) plusOrMinus eps)
            sinFn.calculate(x) shouldBe ((-sinFn.calculate(-x)) plusOrMinus eps)
        }
    }

    "sin and cos should be periodic with 2PI (basic points)" {
        val x = PI / 6
        sinFn.calculate(x) shouldBe (sinFn.calculate(x + 2 * PI) plusOrMinus eps)
        cosFn.calculate(x) shouldBe (cosFn.calculate(x + 2 * PI) plusOrMinus eps)
    }
})

