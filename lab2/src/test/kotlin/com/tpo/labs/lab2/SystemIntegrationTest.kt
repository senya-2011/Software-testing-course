package com.tpo.labs.lab2

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin

class SystemIntegrationTest : StringSpec({
    val eps = 1e-4

    "log branch with our implementation should match oracle for x>0" {
        val system = SystemOfFunctions()

        fun oracle(x: Double): Double {
            val log2 = ln(x) / ln(2.0)
            val log3 = ln(x) / ln(3.0)
            val log5 = ln(x) / ln(5.0)
            val base = ((log2 / log5) - ln(x)) / log3
            return base * base * base / log3
        }

        listOf(0.2, 0.5, 1.3, 2.0).forEach { x ->
            system.calculate(x) shouldBe (oracle(x) plusOrMinus eps)
        }
    }

    "log branch with ideal logs (DI) should match oracle for x>0" {
        val lnF: (Double) -> Double = { x -> ln(x) }
        val log2F: (Double) -> Double = { x -> ln(x) / ln(2.0) }
        val log3F: (Double) -> Double = { x -> ln(x) / ln(3.0) }
        val log5F: (Double) -> Double = { x -> ln(x) / ln(5.0) }

        val constZeroTrig: (Double) -> Double = { 0.0 } // треги здесь не используются для x>0

        val system = SystemOfFunctions(
            constZeroTrig::invoke,
            constZeroTrig::invoke,
            constZeroTrig::invoke,
            constZeroTrig::invoke,
            constZeroTrig::invoke,
            constZeroTrig::invoke,
            lnF::invoke,
            log2F::invoke,
            log3F::invoke,
            log5F::invoke
        )

        fun oracle(x: Double): Double {
            val log2 = ln(x) / ln(2.0)
            val log3 = ln(x) / ln(3.0)
            val log5 = ln(x) / ln(5.0)
            val base = ((log2 / log5) - ln(x)) / log3
            return base * base * base / log3
        }

        listOf(0.2, 0.5, 1.3, 2.0).forEach { x ->
            system.calculate(x) shouldBe (oracle(x) plusOrMinus eps)
        }
    }

    "trig branch with ideal trigs (DI) should match oracle for x<=0" {
        val cosF: (Double) -> Double = { x -> cos(x) }
        val sinF: (Double) -> Double = { x -> sin(x) }
        val secF: (Double) -> Double = { x -> 1.0 / cos(x) }
        val cscF: (Double) -> Double = { x -> 1.0 / sin(x) }
        val tanF: (Double) -> Double = { x -> sin(x) / cos(x) }
        val cotF: (Double) -> Double = { x -> cos(x) / sin(x) }

        val lnF: (Double) -> Double = { x -> ln(x) }
        val log2F: (Double) -> Double = { x -> ln(x) / ln(2.0) }
        val log3F: (Double) -> Double = { x -> ln(x) / ln(3.0) }
        val log5F: (Double) -> Double = { x -> ln(x) / ln(5.0) }

        val system = SystemOfFunctions(
            cosF::invoke,
            sinF::invoke,
            secF::invoke,
            cscF::invoke,
            tanF::invoke,
            cotF::invoke,
            lnF::invoke,
            log2F::invoke,
            log3F::invoke,
            log5F::invoke
        )

        fun oracle(x: Double): Double {
            val sinX = sin(x)
            val cosX = cos(x)
            val tanX = sinX / cosX
            val cotX = cosX / sinX
            val secX = 1.0 / cosX
            val cscX = 1.0 / sinX

            val base = (secX * cscX) / (tanX * tanX)
            val a = base * base * base
            val b = a - (cscX - tanX)
            val c = b * b * b
            val d = c * c
            val e = d * d * d
            val left = e * e * e

            val inner = (cosX - secX) - ((cscX * cscX * cscX) * ((sinX * sinX) * secX))
            val denom = cscX + (((tanX + tanX) * inner) * ((cosX / cotX) + (cotX * (cotX - tanX))))
            return left / denom
        }

        listOf(-PI / 3, -PI / 4).forEach { x ->
            system.calculate(x) shouldBe (oracle(x) plusOrMinus eps)
        }
    }

    "trig branch with our implementation should match oracle for x<=0" {
        val system = SystemOfFunctions()

        fun oracle(x: Double): Double {
            val sinX = sin(x)
            val cosX = cos(x)
            val tanX = sinX / cosX
            val cotX = cosX / sinX
            val secX = 1.0 / cosX
            val cscX = 1.0 / sinX

            val base = (secX * cscX) / (tanX * tanX)
            val a = base * base * base
            val b = a - (cscX - tanX)
            val c = b * b * b
            val d = c * c
            val e = d * d * d
            val left = e * e * e

            val inner = (cosX - secX) - ((cscX * cscX * cscX) * ((sinX * sinX) * secX))
            val denom = cscX + (((tanX + tanX) * inner) * ((cosX / cotX) + (cotX * (cotX - tanX))))
            return left / denom
        }

        listOf(-PI / 3, -PI / 4).forEach { x ->
            val value = system.calculate(x)
            val expected = oracle(x)
            val tol = kotlin.math.abs(expected) * 1e-10
            value shouldBe (expected plusOrMinus tol)
        }
    }
})

