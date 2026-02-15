plugins {
    id("java")
    kotlin("jvm")
}

group = "org.tpo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:6.1.3")
    testImplementation("io.kotest:kotest-assertions-core-jvm:6.1.3")
    testImplementation("io.kotest:kotest-property:6.1.3")

    testImplementation("io.mockk:mockk:1.14.9")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}