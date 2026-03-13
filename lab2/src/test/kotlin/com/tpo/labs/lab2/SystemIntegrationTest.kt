package com.tpo.labs.lab2

import com.tpo.labs.lab2.log.LnFunction
import com.tpo.labs.lab2.log.LogBaseFunction
import com.tpo.labs.lab2.log.LogarithmicFunction
import com.tpo.labs.lab2.trig.CosFunction
import com.tpo.labs.lab2.trig.CotFunction
import com.tpo.labs.lab2.trig.CscFunction
import com.tpo.labs.lab2.trig.SecFunction
import com.tpo.labs.lab2.trig.SinFunction
import com.tpo.labs.lab2.trig.TanFunction
import com.tpo.labs.lab2.trig.TrigonometricFunction
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import kotlin.math.abs

class SystemIntegrationTest : StringSpec({
    "system works with in-memory tabular stubs for all modules" {
        val system = SystemOfFunctions(
            trigStubFromTable("cos", TestTables.cosSystemValues),
            trigStubFromTable("sin", TestTables.sinSystemValues),
            trigStubFromTable("sec", TestTables.secSystemValues),
            trigStubFromTable("csc", TestTables.cscSystemValues),
            trigStubFromTable("tan", TestTables.tanSystemValues),
            trigStubFromTable("cot", TestTables.cotSystemValues),
            logStubFromTable("ln", TestTables.lnSystemValues),
            logStubFromTable("log2", TestTables.log2SystemValues),
            logStubFromTable("log3", TestTables.log3SystemValues),
            logStubFromTable("log5", TestTables.log5SystemValues)
        )

        assertSystemValues(system)
    }

    "system works with real sin and tabular cosine stub" {
        val sin = SinFunction()
        val cos = trigStubFromCalculatedValues(CosFunction(sin), TestTables.trigSystemDomainPoints)
        val system = SystemOfFunctions(
            cos,
            sin,
            trigStubFromTable("sec", TestTables.secSystemValues),
            trigStubFromTable("csc", TestTables.cscSystemValues),
            trigStubFromTable("tan", TestTables.tanSystemValues),
            trigStubFromTable("cot", TestTables.cotSystemValues),
            logStubFromTable("ln", TestTables.lnSystemValues),
            logStubFromTable("log2", TestTables.log2SystemValues),
            logStubFromTable("log3", TestTables.log3SystemValues),
            logStubFromTable("log5", TestTables.log5SystemValues)
        )

        assertSystemValues(system)
    }

    "system works with real trigonometric modules and tabular cosine dependency" {
        val sin = SinFunction()
        val cos = trigStubFromCalculatedValues(CosFunction(sin), TestTables.trigSystemDomainPoints)
        val sec = SecFunction(cos)
        val csc = CscFunction(sin)
        val tan = TanFunction(sin, cos)
        val cot = CotFunction(sin, cos)
        val system = SystemOfFunctions(
            cos,
            sin,
            sec,
            csc,
            tan,
            cot,
            logStubFromTable("ln", TestTables.lnSystemValues),
            logStubFromTable("log2", TestTables.log2SystemValues),
            logStubFromTable("log3", TestTables.log3SystemValues),
            logStubFromTable("log5", TestTables.log5SystemValues)
        )

        assertSystemValues(system)
    }

    "system works with fully real trigonometric branch and tabular logarithmic stubs" {
        val sin = SinFunction()
        val cos = CosFunction(sin)
        val system = SystemOfFunctions(
            cos,
            sin,
            SecFunction(cos),
            CscFunction(sin),
            TanFunction(sin, cos),
            CotFunction(sin, cos),
            logStubFromTable("ln", TestTables.lnSystemValues),
            logStubFromTable("log2", TestTables.log2SystemValues),
            logStubFromTable("log3", TestTables.log3SystemValues),
            logStubFromTable("log5", TestTables.log5SystemValues)
        )

        assertSystemValues(system)
    }

    "system works with real ln and tabular logarithms built over it" {
        val sin = SinFunction()
        val cos = CosFunction(sin)
        val ln = LnFunction()
        val log2 = logStubFromCalculatedValues(LogBaseFunction(ln, 2.0), TestTables.logSystemDomainPoints)
        val log3 = logStubFromCalculatedValues(LogBaseFunction(ln, 3.0), TestTables.logSystemDomainPoints)
        val log5 = logStubFromCalculatedValues(LogBaseFunction(ln, 5.0), TestTables.logSystemDomainPoints)
        val system = SystemOfFunctions(
            cos,
            sin,
            SecFunction(cos),
            CscFunction(sin),
            TanFunction(sin, cos),
            CotFunction(sin, cos),
            ln,
            log2,
            log3,
            log5
        )

        assertSystemValues(system)
    }

    "system works with fully real modules" {
        assertSystemValues(SystemOfFunctions())
    }
})

private fun trigStubFromTable(moduleName: String, values: Map<Double, Double>): TrigonometricFunction {
    return TabularTrigStub(moduleName, values)
}

private fun logStubFromTable(moduleName: String, values: Map<Double, Double>): LogarithmicFunction {
    return TabularLogStub(moduleName, values)
}

private fun trigStubFromCalculatedValues(
    function: TrigonometricFunction,
    points: List<Double>
): TrigonometricFunction {
    return TabularTrigStub(
        "calculated-trig",
        points.associateWith(function::calculate)
    )
}

private fun logStubFromCalculatedValues(
    function: LogarithmicFunction,
    points: List<Double>
): LogarithmicFunction {
    return TabularLogStub(
        "calculated-log",
        points.associateWith(function::calculate)
    )
}

private fun assertSystemValues(system: SystemOfFunctions) {
    TestTables.systemTrigValues.forEach { (x, expected) ->
        system.calculate(x) shouldBe (expected plusOrMinus systemTolerance(expected))
    }

    TestTables.systemLogValues.forEach { (x, expected) ->
        system.calculate(x) shouldBe (expected plusOrMinus systemTolerance(expected))
    }
}

private fun systemTolerance(expected: Double): Double {
    return maxOf(abs(expected) * 1e-4, 1e-6)
}

private class TabularTrigStub(
    private val moduleName: String,
    private val values: Map<Double, Double>
) : TrigonometricFunction {
    override fun calculate(x: Double): Double {
        return values.entries.firstOrNull { kotlin.math.abs(it.key - x) <= TestTables.tableLookupEps }?.value
            ?: throw IllegalArgumentException("No tabular value for " + moduleName + " at x=" + x)
    }
}

private class TabularLogStub(
    private val moduleName: String,
    private val values: Map<Double, Double>
) : LogarithmicFunction {
    override fun calculate(x: Double): Double {
        return values.entries.firstOrNull { kotlin.math.abs(it.key - x) <= TestTables.tableLookupEps }?.value
            ?: throw IllegalArgumentException("No tabular value for " + moduleName + " at x=" + x)
    }
}
