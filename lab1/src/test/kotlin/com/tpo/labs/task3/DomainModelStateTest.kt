package com.tpo.labs.task3

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class DomainModelStateTest : BehaviorSpec({

    Given("Entity Arthur") {
        When("created with a name only") {
            val arthur = Arthur("Артур")

            Then("it has default initial state") {
                arthur.name shouldBe "Артур"
                arthur.head.isCorporeal shouldBe false
                arthur.isConfused shouldBe false
                arthur.movementTarget shouldBe null
                arthur.lastSensation shouldBe null
                arthur.lastRealizationCause shouldBe null
                arthur.head.isHairMoving shouldBe false
                arthur.head.hairMovementLevel shouldBe 0
            }
        }

        When("created with a specific Head") {
            val head = Head(true)
            val arthur = Arthur("Артур", head)

            Then("it keeps reference to the provided head") {
                (arthur.head === head) shouldBe true
                arthur.head.isCorporeal shouldBe true
            }
        }

        When("created with arbitrary names") {
            Then("it preserves the name correctly") {
                checkAll(Arb.string(minSize = 0, maxSize = 40)) { name ->
                    Arthur(name).name shouldBe name
                }
            }
        }

        When("initialized with null arguments") {
            Then("it throws NullPointerException") {
                shouldThrow<NullPointerException> { Arthur(null) }
                shouldThrow<NullPointerException> { Arthur("Артур", null) }
            }
        }

        When("methods are called with null arguments") {
            val arthur = Arthur("Артур")
            Then("it throws NullPointerException") {
                shouldThrow<NullPointerException> { arthur.feelSensation(null) }
                shouldThrow<NullPointerException> { arthur.perceiveMovementTo(null) }
                shouldThrow<NullPointerException> { arthur.realizeTruth(null) }
            }
        }
    }

    Given("Entity Head") {
        When("created with arbitrary corporeal flag") {
            Then("it preserves the state and defaults dynamic properties") {
                checkAll(Arb.boolean()) { corporeal ->
                    val head = Head(corporeal)
                    head.isCorporeal shouldBe corporeal
                    head.isHairMoving shouldBe false
                    head.hairMovementLevel shouldBe 0
                }
            }
        }

        When("startHairMovement is called with positive level") {
            Then("hair starts moving with correct level") {
                checkAll(Arb.int(1, 100)) { level ->
                    val head = Head(false)
                    head.startHairMovement(level)
                    head.isHairMoving shouldBe true
                    head.hairMovementLevel shouldBe level
                }
            }
        }

        When("startHairMovement is called with invalid level") {
            val head = Head(false)
            Then("it throws IllegalArgumentException for zero or negative") {
                shouldThrow<IllegalArgumentException> { head.startHairMovement(0) }
                shouldThrow<IllegalArgumentException> { head.startHairMovement(-1) }
            }
        }

        When("stopHairMovement is called") {
            val head = Head(false)
            head.startHairMovement(5)
            head.stopHairMovement()

            Then("hair movement stops and level resets") {
                head.isHairMoving shouldBe false
                head.hairMovementLevel shouldBe 0
            }
        }
    }

    Given("Entity Console") {
        When("created with defaults") {
            val console = Console()
            Then("it has default description and is active") {
                console.description shouldBe "Пульт"
                console.isActive shouldBe true
                console.toString() shouldBe "Пульт"
            }
        }

        When("created with arbitrary description") {
            Then("it preserves description") {
                checkAll(Arb.string(minSize = 0, maxSize = 40)) { desc ->
                    val console = Console(desc)
                    console.description shouldBe desc
                    console.toString() shouldBe desc
                }
            }
        }

        When("created with null description") {
            Then("it throws NullPointerException") {
                shouldThrow<NullPointerException> { Console(null) }
            }
        }

        When("setActive is called") {
            Then("state updates accordingly") {
                checkAll(Arb.boolean()) { active ->
                    val console = Console()
                    console.isActive = active
                    console.isActive shouldBe active
                }
            }
        }
    }

    Given("Entity Camera") {

        When("created with defaults") {
            val camera = Camera()
            Then("it is recording and zoom is 1") {
                camera.isRecording shouldBe true
                camera.zoomLevel shouldBe 1
            }
        }

        When("created with recording flag") {
            Then("it preserves the flag") {
                checkAll(Arb.boolean()) { recording ->
                    Camera(recording).isRecording shouldBe recording
                }
            }
        }

        When("setRecording is modified") {
            Then("state updates") {
                checkAll(Arb.boolean()) { recording ->
                    val camera = Camera()
                    camera.isRecording = recording
                    camera.isRecording shouldBe recording
                }
            }
        }
    }

    Given("A scene with Arthur, Console and Camera") {

        When("Camera performs dolly-in (Standard Success Flow)") {
            val arthur = Arthur("Артур")
            val console = Console()
            val camera = Camera()

            val performed = camera.performDollyIn(arthur, console)

            Then("action returns true") {
                performed shouldBe true
            }

            Then("Camera zooms in") {
                camera.zoomLevel shouldBe 2
            }

            Then("Arthur reacts to the movement") {
                arthur.movementTarget shouldBe console
                arthur.lastSensation shouldBe "волосы на голове зашевелились"
                arthur.lastRealizationCause shouldBe "наезд камеры при съемке"
                arthur.isConfused shouldBe false
            }

            Then("Arthur's physical reaction is suppressed (hair stops)") {
                arthur.head.isHairMoving shouldBe false
                arthur.head.hairMovementLevel shouldBe 0
            }
        }

        When("Camera is NOT recording") {
            val arthur = Arthur("Артур")
            val console = Console()
            val camera = Camera(false) // Not recording

            val performed = camera.performDollyIn(arthur, console)

            Then("action is ignored (false)") {
                performed shouldBe false
            }
            Then("no state changes occur") {
                camera.zoomLevel shouldBe 1
                arthur.movementTarget shouldBe null
                arthur.lastSensation shouldBe null
            }
        }

        When("Target Console is inactive") {
            val arthur = Arthur("Артур")
            val console = Console().apply { isActive = false }
            val camera = Camera()

            val performed = camera.performDollyIn(arthur, console)

            Then("action is ignored") {
                performed shouldBe false
                camera.zoomLevel shouldBe 1
                arthur.movementTarget shouldBe null
            }
        }

        When("Camera performs dolly-in multiple times") {
            Then("Zoom increments correctly") {
                checkAll(Arb.int(1, 20)) { calls ->
                    val arthur = Arthur("Артур")
                    val console = Console()
                    val camera = Camera()

                    repeat(calls) { camera.performDollyIn(arthur, console) shouldBe true }

                    camera.zoomLevel shouldBe (1 + calls)
                    arthur.movementTarget shouldBe console
                }
            }
        }

        When("Called with null arguments") {
            val camera = Camera()
            val arthur = Arthur("Артур")
            val console = Console()

            Then("it throws NullPointerException") {
                shouldThrow<NullPointerException> { camera.performDollyIn(null, console) }
                shouldThrow<NullPointerException> { camera.performDollyIn(arthur, null) }
            }
        }
    }
})