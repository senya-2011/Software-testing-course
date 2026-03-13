package com.tpo.labs.lab2

import com.tpo.labs.lab2.io.CsvFunctionExporter
import com.tpo.labs.lab2.table.TableTrigonometricFunction
import com.tpo.labs.lab2.trig.SinFunction
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import java.nio.file.Files
import kotlin.math.PI

class CsvExportTest : StringSpec({
    "exportRange writes header and rows with custom delimiter" {
        val path = Files.createTempFile("sin-range", ".csv")

        CsvFunctionExporter.exportRange(
            path,
            "sin",
            SinFunction()::calculate,
            0.0,
            PI / 3.0,
            PI / 6.0,
            ","
        )

        val lines = Files.readAllLines(path)
        lines shouldHaveSize 4
        lines[0] shouldBe "X,sin(X)"

        val table = TableTrigonometricFunction.fromCsv(path, ",", TestTables.tableLookupEps)
        TestTables.sinRangeCsvValues.forEach { (x, expected) ->
            table.calculate(x) shouldBe (expected plusOrMinus 1e-7)
        }
    }
})
