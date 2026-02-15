package com.tpo.labs

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class DemoTest: StringSpec( {
    "2 + 2 = 4"{
        2 + 2 shouldBe 4
    }
})