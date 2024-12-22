plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
    kotlin("plugin.allopen") version "2.1.0"
}

group = "shreckye"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
        register("main")
    }
}
