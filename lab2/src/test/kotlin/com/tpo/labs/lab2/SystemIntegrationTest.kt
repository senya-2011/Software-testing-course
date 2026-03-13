package com.tpo.labs.lab2

import com.tpo.labs.lab2.io.CsvFunctionExporter
import com.tpo.labs.lab2.log.LnFunction
import com.tpo.labs.lab2.log.LogBaseFunction
import com.tpo.labs.lab2.log.LogarithmicFunction
import com.tpo.labs.lab2.table.TableLogarithmicFunction
import com.tpo.labs.lab2.table.TableTrigonometricFunction
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
import java.nio.file.Files
import java.util.function.DoubleUnaryOperator
import kotlin.math.abs

class SystemIntegrationTest : StringSpec({
    "system works with CSV stubs for all modules" {
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

    "system works with real sin and CSV cosine built over it" {
        val sin = SinFunction()
        val cos = trigStubFromFunction("cos", CosFunction(sin)::calculate, TestTables.trigSystemDomainPoints)
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

    "system works with real trigonometric modules and CSV cosine dependency" {
        val sin = SinFunction()
        val cos = trigStubFromFunction("cos", CosFunction(sin)::calculate, TestTables.trigSystemDomainPoints)
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

    "system works with fully real trigonometric branch and CSV logarithmic stubs" {
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

    "system works with real ln and CSV logarithms built over it" {
        val sin = SinFunction()
        val cos = CosFunction(sin)
        val ln = LnFunction()
        val log2 = logStubFromFunction("log2", LogBaseFunction(ln, 2.0)::calculate, TestTables.logSystemDomainPoints)
        val log3 = logStubFromFunction("log3", LogBaseFunction(ln, 3.0)::calculate, TestTables.logSystemDomainPoints)
        val log5 = logStubFromFunction("log5", LogBaseFunction(ln, 5.0)::calculate, TestTables.logSystemDomainPoints)
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

private const val delimiter = ";"

private fun trigStubFromTable(moduleName: String, values: Map<Double, Double>): TrigonometricFunction {
    return trigStubFromFunction(
        moduleName,
        DoubleUnaryOperator { x -> values.getValue(x) },
        values.keys.sorted()
    )
}

private fun logStubFromTable(moduleName: String, values: Map<Double, Double>): LogarithmicFunction {
    return logStubFromFunction(
        moduleName,
        DoubleUnaryOperator { x -> values.getValue(x) },
        values.keys.sorted()
    )
}

private fun trigStubFromFunction(
    moduleName: String,
    function: DoubleUnaryOperator,
    points: List<Double>
): TrigonometricFunction {
    val path = Files.createTempFile(moduleName, ".csv")
    CsvFunctionExporter.exportPoints(path, moduleName, function, points, delimiter)
    return TableTrigonometricFunction.fromCsv(path, delimiter, TestTables.tableLookupEps)
}

private fun logStubFromFunction(
    moduleName: String,
    function: DoubleUnaryOperator,
    points: List<Double>
): LogarithmicFunction {
    val path = Files.createTempFile(moduleName, ".csv")
    CsvFunctionExporter.exportPoints(path, moduleName, function, points, delimiter)
    return TableLogarithmicFunction.fromCsv(path, delimiter, TestTables.tableLookupEps)
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
