plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}