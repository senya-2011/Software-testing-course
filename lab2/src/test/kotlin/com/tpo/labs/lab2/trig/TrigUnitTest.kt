package com.tpo.labs.lab2.trig

import com.tpo.labs.lab2.TestTables
import com.tpo.labs.lab2.table.TableTrigonometricFunction
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import kotlin.math.PI

class TrigUnitTest : StringSpec({
    val trigEps = 1e-7
    val tableEps = TestTables.tableLookupEps

    "sin returns tabular values on control points" {
        val sinFunction = SinFunction()

        TestTables.sinControlValues.forEach { (x, expected) ->
            sinFunction.calculate(x) shouldBe (expected plusOrMinus trigEps)
        }
    }

    "sin is odd on paired tabular points" {
        val sinFunction = SinFunction()

        TestTables.symmetryPoints.forEach { x ->
            sinFunction.calculate(x) shouldBe (-sinFunction.calculate(-x) plusOrMinus trigEps)
        }
    }

    "sin is periodic with period 2pi on control points" {
        val sinFunction = SinFunction()

        TestTables.periodicPoints.forEach { x ->
            sinFunction.calculate(x) shouldBe (sinFunction.calculate(x + 2 * PI) plusOrMinus trigEps)
        }
    }

    "cos delegates to sin with phase shift" {
        var delegatedX: Double? = null
        val cosFunction = CosFunction { x ->
            delegatedX = x
            0.25
        }

        cosFunction.calculate(0.4) shouldBe 0.25
        delegatedX!! shouldBe ((PI / 2.0) - 0.4 plusOrMinus tableEps)
    }

    "cos returns tabular values on control points" {
        val cosFunction = CosFunction(SinFunction())

        TestTables.cosControlValues.forEach { (x, expected) ->
            cosFunction.calculate(x) shouldBe (expected plusOrMinus trigEps)
        }
    }

    "cos is even on paired tabular points" {
        val cosFunction = CosFunction(SinFunction())

        TestTables.symmetryPoints.forEach { x ->
            cosFunction.calculate(x) shouldBe (cosFunction.calculate(-x) plusOrMinus trigEps)
        }
    }

    "sec uses tabular cosine stub" {
        val secFunction = SecFunction(TableTrigonometricFunction(TestTables.cosControlValues, tableEps))

        TestTables.secControlValues.forEach { (x, expected) ->
            secFunction.calculate(x) shouldBe (expected plusOrMinus trigEps)
        }
    }

    "sec returns infinity on cosine domain boundary" {
        val secFunction = SecFunction(TableTrigonometricFunction(mapOf(PI / 2.0 to 0.0), tableEps))

        secFunction.calculate(PI / 2.0).isInfinite() shouldBe true
    }

    "csc uses tabular sine stub" {
        val cscFunction = CscFunction(TableTrigonometricFunction(TestTables.sinControlValues, tableEps))

        TestTables.cscControlValues.forEach { (x, expected) ->
            cscFunction.calculate(x) shouldBe (expected plusOrMinus trigEps)
        }
    }

    "csc returns infinity on sine domain boundary" {
        val cscFunction = CscFunction(TableTrigonometricFunction(mapOf(0.0 to 0.0), tableEps))

        cscFunction.calculate(0.0).isInfinite() shouldBe true
    }

    "tan uses tabular sine and cosine stubs" {
        val sinStub = TableTrigonometricFunction(TestTables.sinControlValues, tableEps)
        val cosStub = TableTrigonometricFunction(TestTables.cosControlValues, tableEps)
        val tanFunction = TanFunction(sinStub, cosStub)

        TestTables.tanControlValues.forEach { (x, expected) ->
            tanFunction.calculate(x) shouldBe (expected plusOrMinus trigEps)
        }
    }

    "tan returns infinity on cosine domain boundary" {
        val sinStub = TableTrigonometricFunction(mapOf(PI / 2.0 to 1.0), tableEps)
        val cosStub = TableTrigonometricFunction(mapOf(PI / 2.0 to 0.0), tableEps)
        val tanFunction = TanFunction(sinStub, cosStub)

        tanFunction.calculate(PI / 2.0).isInfinite() shouldBe true
    }

    "cot uses tabular sine and cosine stubs" {
        val sinStub = TableTrigonometricFunction(TestTables.sinControlValues, tableEps)
        val cosStub = TableTrigonometricFunction(TestTables.cosControlValues, tableEps)
        val cotFunction = CotFunction(sinStub, cosStub)

        TestTables.cotControlValues.forEach { (x, expected) ->
            cotFunction.calculate(x) shouldBe (expected plusOrMinus trigEps)
        }
    }

    "cot returns infinity on sine domain boundary" {
        val sinStub = TableTrigonometricFunction(mapOf(0.0 to 0.0), tableEps)
        val cosStub = TableTrigonometricFunction(mapOf(0.0 to 1.0), tableEps)
        val cotFunction = CotFunction(sinStub, cosStub)

        cotFunction.calculate(0.0).isInfinite() shouldBe true
    }
})
