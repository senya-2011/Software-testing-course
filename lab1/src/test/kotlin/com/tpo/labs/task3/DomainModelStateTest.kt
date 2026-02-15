package com.tpo.labs.task3

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class DomainModelStateTest :
        StringSpec({
            "Arthur should initialize default state via single-argument constructor" {
                val arthur = Arthur("Артур")

                arthur.name shouldBe "Артур"
                arthur.head.isCorporeal shouldBe false
                arthur.isConfused shouldBe false
                arthur.movementTarget shouldBe null
                arthur.lastSensation shouldBe null
                arthur.lastRealizationCause shouldBe null
                arthur.head.isHairMoving shouldBe false
                arthur.head.hairMovementLevel shouldBe 0
            }

            "Arthur(name, head) should keep reference to provided head" {
                val head = Head(true)
                val arthur = Arthur("Артур", head)

                (arthur.head === head) shouldBe true
                arthur.head.isCorporeal shouldBe true
            }

            "Arthur should preserve any generated name" {
                checkAll(Arb.string(minSize = 0, maxSize = 40)) { name ->
                    Arthur(name).name shouldBe name
                }
            }

            "Arthur should throw NullPointerException for null constructor args" {
                val nullName: String? = null
                val nullHead: Head? = null

                shouldThrow<NullPointerException> { Arthur(nullName) }
                shouldThrow<NullPointerException> { Arthur("Артур", nullHead) }
            }

            "Arthur methods should throw NullPointerException for null inputs" {
                val arthur = Arthur("Артур")
                val nullText: String? = null
                val nullConsole: Console? = null

                shouldThrow<NullPointerException> { arthur.feelSensation(nullText) }
                shouldThrow<NullPointerException> { arthur.perceiveMovementTo(nullConsole) }
                shouldThrow<NullPointerException> { arthur.realizeTruth(nullText) }
            }

            "Head should preserve corporeal flag and default dynamic state" {
                checkAll(Arb.boolean()) { corporeal ->
                    val head = Head(corporeal)

                    head.isCorporeal shouldBe corporeal
                    head.isHairMoving shouldBe false
                    head.hairMovementLevel shouldBe 0
                }
            }

            "Head should start hair movement for positive levels" {
                checkAll(Arb.int(1, 100)) { level ->
                    val head = Head(false)
                    head.startHairMovement(level)

                    head.isHairMoving shouldBe true
                    head.hairMovementLevel shouldBe level
                }
            }

            "Head should reject non-positive hair movement level" {
                val head = Head(false)

                shouldThrow<IllegalArgumentException> { head.startHairMovement(0) }
                shouldThrow<IllegalArgumentException> { head.startHairMovement(-1) }
            }

            "Head should stop hair movement and reset level" {
                val head = Head(false)
                head.startHairMovement(5)

                head.stopHairMovement()

                head.isHairMoving shouldBe false
                head.hairMovementLevel shouldBe 0
            }

            "Console should initialize default state" {
                val console = Console()

                console.description shouldBe "Пульт"
                console.isActive shouldBe true
                console.toString() shouldBe "Пульт"
            }

            "Console should preserve custom description for generated values" {
                checkAll(Arb.string(minSize = 0, maxSize = 40)) { description ->
                    val console = Console(description)
                    console.description shouldBe description
                    console.toString() shouldBe description
                }
            }

            "Console should throw NullPointerException for null description" {
                val nullDescription: String? = null
                shouldThrow<NullPointerException> { Console(nullDescription) }
            }

            "Console active flag should follow setActive value" {
                checkAll(Arb.boolean()) { active ->
                    val console = Console()
                    console.isActive = active
                    console.isActive shouldBe active
                }
            }

            "Camera should initialize default state" {
                val camera = Camera()

                camera.isRecording shouldBe true
                camera.zoomLevel shouldBe 1
            }

            "Camera(recording) should preserve constructor flag for generated values" {
                checkAll(Arb.boolean()) { recording ->
                    val camera = Camera(recording)
                    camera.isRecording shouldBe recording
                    camera.zoomLevel shouldBe 1
                }
            }

            "Camera setRecording should update state" {
                checkAll(Arb.boolean()) { recording ->
                    val camera = Camera()
                    camera.isRecording = recording
                    camera.isRecording shouldBe recording
                }
            }

            "performDollyIn should update internal state of all objects" {
                val arthur = Arthur("Артур")
                val console = Console()
                val camera = Camera()

                val performed = camera.performDollyIn(arthur, console)

                performed shouldBe true
                camera.zoomLevel shouldBe 2

                arthur.movementTarget shouldBe console
                arthur.lastSensation shouldBe "волосы на голове зашевелились"
                arthur.lastRealizationCause shouldBe "наезд камеры при съемке"
                arthur.isConfused shouldBe false

                arthur.head.isHairMoving shouldBe false
                arthur.head.hairMovementLevel shouldBe 0
            }

            "performDollyIn should do nothing when camera is not recording" {
                val arthur = Arthur("Артур")
                val console = Console()
                val camera = Camera(false)

                val performed = camera.performDollyIn(arthur, console)

                performed shouldBe false
                camera.zoomLevel shouldBe 1

                arthur.movementTarget shouldBe null
                arthur.lastSensation shouldBe null
                arthur.lastRealizationCause shouldBe null
                arthur.isConfused shouldBe false

                arthur.head.isHairMoving shouldBe false
                arthur.head.hairMovementLevel shouldBe 0
            }

            "performDollyIn should do nothing when console is inactive" {
                val arthur = Arthur("Артур")
                val console = Console().apply { isActive = false }
                val camera = Camera()

                val performed = camera.performDollyIn(arthur, console)

                performed shouldBe false
                camera.zoomLevel shouldBe 1
                arthur.movementTarget shouldBe null
            }

            "performDollyIn should increment zoom by one for each successful call" {
                checkAll(Arb.int(1, 20)) { successfulCalls ->
                    val arthur = Arthur("Артур")
                    val console = Console()
                    val camera = Camera()

                    repeat(successfulCalls) { camera.performDollyIn(arthur, console) shouldBe true }

                    camera.zoomLevel shouldBe (1 + successfulCalls)
                    arthur.movementTarget shouldBe console
                    arthur.lastSensation shouldBe "волосы на голове зашевелились"
                    arthur.lastRealizationCause shouldBe "наезд камеры при съемке"
                    arthur.isConfused shouldBe false
                    arthur.head.isHairMoving shouldBe false
                    arthur.head.hairMovementLevel shouldBe 0
                }
            }

            "performDollyIn should throw NullPointerException for null subject or target" {
                val camera = Camera()
                val arthur = Arthur("Артур")
                val console = Console()
                val nullArthur: Arthur? = null
                val nullConsole: Console? = null

                shouldThrow<NullPointerException> { camera.performDollyIn(nullArthur, console) }
                shouldThrow<NullPointerException> { camera.performDollyIn(arthur, nullConsole) }
            }
        })
