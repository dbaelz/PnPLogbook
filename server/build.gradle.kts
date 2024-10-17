plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    application
}

group = "de.dbaelz.pnp.logbook"
version = "1.0.0"
application {
    mainClass.set("de.dbaelz.pnp.logbook.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.serialization.json.jvm)
    implementation(libs.ktor.server.contentnegotiation)
    implementation(libs.ktor.exposed.core)
    implementation(libs.ktor.exposed.jdbc)
    implementation(libs.sqlite)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}