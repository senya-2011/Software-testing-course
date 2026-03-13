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
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.math.abs

class SystemIntegrationTest : StringSpec({
    "system works with in-memory tabular mocks for all modules" {
        val cos = trigMockFromTable(TestTables.cosSystemValues)
        val sin = trigMockFromTable(TestTables.sinSystemValues)
        val sec = trigMockFromTable(TestTables.secSystemValues)
        val csc = trigMockFromTable(TestTables.cscSystemValues)
        val tan = trigMockFromTable(TestTables.tanSystemValues)
        val cot = trigMockFromTable(TestTables.cotSystemValues)
        val ln = logMockFromTable(TestTables.lnSystemValues)
        val log2 = logMockFromTable(TestTables.log2SystemValues)
        val log3 = logMockFromTable(TestTables.log3SystemValues)
        val log5 = logMockFromTable(TestTables.log5SystemValues)
        val system = SystemOfFunctions(
            cos.mock,
            sin.mock,
            sec.mock,
            csc.mock,
            tan.mock,
            cot.mock,
            ln.mock,
            log2.mock,
            log3.mock,
            log5.mock
        )

        assertSystemValues(system)
        verifyTrigCalls(cos, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(sin, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(sec, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(csc, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(tan, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(cot, TestTables.trigSystemDomainPoints)
        verifyLogCalls(ln, TestTables.logSystemDomainPoints)
        verifyLogCalls(log2, TestTables.logSystemDomainPoints)
        verifyLogCalls(log3, TestTables.logSystemDomainPoints)
        verifyLogCalls(log5, TestTables.logSystemDomainPoints)
    }

    "system works with real sin and mocked cosine table" {
        val sin = SinFunction()
        val cos = trigMockFromCalculatedValues(CosFunction(sin), TestTables.trigSystemDomainPoints)
        val sec = trigMockFromTable(TestTables.secSystemValues)
        val csc = trigMockFromTable(TestTables.cscSystemValues)
        val tan = trigMockFromTable(TestTables.tanSystemValues)
        val cot = trigMockFromTable(TestTables.cotSystemValues)
        val ln = logMockFromTable(TestTables.lnSystemValues)
        val log2 = logMockFromTable(TestTables.log2SystemValues)
        val log3 = logMockFromTable(TestTables.log3SystemValues)
        val log5 = logMockFromTable(TestTables.log5SystemValues)
        val system = SystemOfFunctions(
            cos.mock,
            sin,
            sec.mock,
            csc.mock,
            tan.mock,
            cot.mock,
            ln.mock,
            log2.mock,
            log3.mock,
            log5.mock
        )

        assertSystemValues(system)
        verifyTrigCalls(cos, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(sec, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(csc, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(tan, TestTables.trigSystemDomainPoints)
        verifyTrigCalls(cot, TestTables.trigSystemDomainPoints)
        verifyLogCalls(ln, TestTables.logSystemDomainPoints)
        verifyLogCalls(log2, TestTables.logSystemDomainPoints)
        verifyLogCalls(log3, TestTables.logSystemDomainPoints)
        verifyLogCalls(log5, TestTables.logSystemDomainPoints)
    }

    "system works with real trigonometric modules and mocked cosine dependency" {
        val sin = SinFunction()
        val cos = trigMockFromCalculatedValues(CosFunction(sin), TestTables.trigSystemDomainPoints)
        val ln = logMockFromTable(TestTables.lnSystemValues)
        val log2 = logMockFromTable(TestTables.log2SystemValues)
        val log3 = logMockFromTable(TestTables.log3SystemValues)
        val log5 = logMockFromTable(TestTables.log5SystemValues)
        val sec = SecFunction(cos.mock)
        val csc = CscFunction(sin)
        val tan = TanFunction(sin, cos.mock)
        val cot = CotFunction(sin, cos.mock)
        val system = SystemOfFunctions(
            cos.mock,
            sin,
            sec,
            csc,
            tan,
            cot,
            ln.mock,
            log2.mock,
            log3.mock,
            log5.mock
        )

        assertSystemValues(system)
        verifyTrigCalls(cos, TestTables.trigSystemDomainPoints, exactly = 4)
        verifyLogCalls(ln, TestTables.logSystemDomainPoints)
        verifyLogCalls(log2, TestTables.logSystemDomainPoints)
        verifyLogCalls(log3, TestTables.logSystemDomainPoints)
        verifyLogCalls(log5, TestTables.logSystemDomainPoints)
    }

    "system works with fully real trigonometric branch and mocked logarithmic tables" {
        val sin = SinFunction()
        val cos = CosFunction(sin)
        val ln = logMockFromTable(TestTables.lnSystemValues)
        val log2 = logMockFromTable(TestTables.log2SystemValues)
        val log3 = logMockFromTable(TestTables.log3SystemValues)
        val log5 = logMockFromTable(TestTables.log5SystemValues)
        val system = SystemOfFunctions(
            cos,
            sin,
            SecFunction(cos),
            CscFunction(sin),
            TanFunction(sin, cos),
            CotFunction(sin, cos),
            ln.mock,
            log2.mock,
            log3.mock,
            log5.mock
        )

        assertSystemValues(system)
        verifyLogCalls(ln, TestTables.logSystemDomainPoints)
        verifyLogCalls(log2, TestTables.logSystemDomainPoints)
        verifyLogCalls(log3, TestTables.logSystemDomainPoints)
        verifyLogCalls(log5, TestTables.logSystemDomainPoints)
    }

    "system works with real ln and mocked logarithms built over it" {
        val sin = SinFunction()
        val cos = CosFunction(sin)
        val ln = LnFunction()
        val log2 = logMockFromCalculatedValues(LogBaseFunction(ln, 2.0), TestTables.logSystemDomainPoints)
        val log3 = logMockFromCalculatedValues(LogBaseFunction(ln, 3.0), TestTables.logSystemDomainPoints)
        val log5 = logMockFromCalculatedValues(LogBaseFunction(ln, 5.0), TestTables.logSystemDomainPoints)
        val system = SystemOfFunctions(
            cos,
            sin,
            SecFunction(cos),
            CscFunction(sin),
            TanFunction(sin, cos),
            CotFunction(sin, cos),
            ln,
            log2.mock,
            log3.mock,
            log5.mock
        )

        assertSystemValues(system)
        verifyLogCalls(log2, TestTables.logSystemDomainPoints)
        verifyLogCalls(log3, TestTables.logSystemDomainPoints)
        verifyLogCalls(log5, TestTables.logSystemDomainPoints)
    }

    "system works with fully real modules" {
        assertSystemValues(SystemOfFunctions())
    }
})

private fun trigMockFromTable(values: Map<Double, Double>): TrigMockFixture {
    val mock = mockk<TrigonometricFunction>()
    values.forEach { (x, y) ->
        every { mock.calculate(x) } returns y
    }
    return TrigMockFixture(mock)
}

private fun logMockFromTable(values: Map<Double, Double>): LogMockFixture {
    val mock = mockk<LogarithmicFunction>()
    values.forEach { (x, y) ->
        every { mock.calculate(x) } returns y
    }
    return LogMockFixture(mock)
}

private fun trigMockFromCalculatedValues(
    function: TrigonometricFunction,
    points: List<Double>
) : TrigMockFixture {
    return trigMockFromTable(
        points.associateWith(function::calculate)
    )
}

private fun logMockFromCalculatedValues(
    function: LogarithmicFunction,
    points: List<Double>
) : LogMockFixture {
    return logMockFromTable(
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

private fun verifyTrigCalls(fixture: TrigMockFixture, points: List<Double>, exactly: Int = 1) {
    points.forEach { x ->
        verify(exactly = exactly) { fixture.mock.calculate(x) }
    }
    confirmVerified(fixture.mock)
}

private fun verifyLogCalls(fixture: LogMockFixture, points: List<Double>, exactly: Int = 1) {
    points.forEach { x ->
        verify(exactly = exactly) { fixture.mock.calculate(x) }
    }
    confirmVerified(fixture.mock)
}

private data class TrigMockFixture(
    val mock: TrigonometricFunction
)

private data class LogMockFixture(
    val mock: LogarithmicFunction
)
