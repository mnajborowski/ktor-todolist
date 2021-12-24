val ktor_version = "1.6.7"
val kotlin_version = "1.5.21"
val logback_version = "1.2.9"
val kotest_version = "4.6.3"
val kotest_koin_version = "1.1.0"
val kotest_assertions_version = "1.0.3"

val postgresql_version = "42.3.1"
val hikari_version = "5.0.0"

val exposed_version = "0.36.1"
val koin_version = "3.1.4"

plugins {
    application
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.21"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.postgresql:postgresql:$postgresql_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
//    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest.extensions:kotest-extensions-koin:$kotest_koin_version")
    testImplementation("io.kotest.extensions:kotest-assertions-ktor:$kotest_assertions_version")
}