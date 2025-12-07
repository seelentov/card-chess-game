plugins {
    id("java")
}

group = "ru.vladislavkomkov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.java-websocket:Java-WebSocket:1.5.4")

    implementation("org.reflections:reflections:0.10.2")

    implementation("io.javalin:javalin:6.1.6")
    implementation("org.slf4j:slf4j-simple:2.0.10")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}