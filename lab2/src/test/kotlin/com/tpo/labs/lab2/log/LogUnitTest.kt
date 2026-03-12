package com.tpo.labs.lab2.log

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.checkAll
import kotlin.math.ln

class LogUnitTest : StringSpec({
    val eps = 1e-4
    val lnFn: LogarithmicFunction = LnFunction()
    val log2 = LogBaseFunction(lnFn, 2.0)
    val log3 = LogBaseFunction(lnFn, 3.0)
    val log5 = LogBaseFunction(lnFn, 5.0)

    "ln(x) should match kotlin ln(x) for x in (0, 5]" {
        checkAll(Arb.double(1e-3, 5.0).filter { it.isFinite() }) { x ->
            lnFn.calculate(x) shouldBe (ln(x) plusOrMinus eps)
        }
    }

    "log_b(x) should satisfy change of base against kotlin ln" {
        checkAll(Arb.double(1e-3, 5.0).filter { it.isFinite() }) { x ->
            log2.calculate(x) shouldBe ((ln(x) / ln(2.0)) plusOrMinus eps)
            log3.calculate(x) shouldBe ((ln(x) / ln(3.0)) plusOrMinus eps)
            log5.calculate(x) shouldBe ((ln(x) / ln(5.0)) plusOrMinus eps)
        }
    }

    "ln(x) should be NaN for x<=0" {
        lnFn.calculate(0.0).isNaN() shouldBe true
        lnFn.calculate(-1.0).isNaN() shouldBe true
    }
})

