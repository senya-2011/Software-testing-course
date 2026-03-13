package com.tpo.labs.lab2.log

import com.tpo.labs.lab2.TestTables
import com.tpo.labs.lab2.table.TableLogarithmicFunction
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

class LogUnitTest : StringSpec({
    val logEps = 1e-7
    val tableEps = TestTables.tableLookupEps

    "ln returns tabular values on control points" {
        val lnFunction = LnFunction()

        TestTables.lnControlValues.forEach { (x, expected) ->
            lnFunction.calculate(x) shouldBe (expected plusOrMinus logEps)
        }
    }

    "ln respects multiplication law on tabular points" {
        val lnFunction = LnFunction()

        val left = lnFunction.calculate(2.0) + lnFunction.calculate(0.5)
        left shouldBe (TestTables.lnControlValues.getValue(1.0) plusOrMinus logEps)
    }

    "ln is undefined for non-positive values" {
        val lnFunction = LnFunction()

        lnFunction.calculate(0.0).isNaN() shouldBe true
        lnFunction.calculate(-1.0).isNaN() shouldBe true
    }

    "log2 uses ln tabular stub" {
        val log2 = LogBaseFunction(TableLogarithmicFunction(TestTables.lnControlValues, tableEps), 2.0)

        TestTables.log2ControlValues.forEach { (x, expected) ->
            log2.calculate(x) shouldBe (expected plusOrMinus logEps)
        }
    }

    "log3 uses ln tabular stub" {
        val log3 = LogBaseFunction(TableLogarithmicFunction(TestTables.lnControlValues, tableEps), 3.0)

        TestTables.log3ControlValues.forEach { (x, expected) ->
            log3.calculate(x) shouldBe (expected plusOrMinus logEps)
        }
    }

    "log5 uses ln tabular stub" {
        val log5 = LogBaseFunction(TableLogarithmicFunction(TestTables.lnControlValues, tableEps), 5.0)

        TestTables.log5ControlValues.forEach { (x, expected) ->
            log5.calculate(x) shouldBe (expected plusOrMinus logEps)
        }
    }

    "logarithms preserve base points" {
        val lnStub = TableLogarithmicFunction(TestTables.lnControlValues, tableEps)

        LogBaseFunction(lnStub, 2.0).calculate(2.0) shouldBe (1.0 plusOrMinus logEps)
        LogBaseFunction(lnStub, 3.0).calculate(3.0) shouldBe (1.0 plusOrMinus logEps)
        LogBaseFunction(lnStub, 5.0).calculate(5.0) shouldBe (1.0 plusOrMinus logEps)
    }
})
