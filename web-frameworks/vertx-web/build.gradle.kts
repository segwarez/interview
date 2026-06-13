import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    java
    application
    id("com.gradleup.shadow") version "9.2.2"
}

group = "com.segwarez"
version = "1.0.0"

repositories {
    mavenCentral()
}

val vertxVersion = "5.1.2"
val micrometerPrometheusVersion = "1.17.0"
val jacksonVersion = "2.22.0"
val lombokVersion = "1.18.38"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.segwarez.vertxrx.ApplicationVerticle"
val launcherClassName = "io.vertx.launcher.application.VertxApplication"

application {
    mainClass.set(launcherClassName)
}

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-web-validation")
    implementation("io.vertx:vertx-pg-client")
    implementation("io.vertx:vertx-micrometer-metrics")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerPrometheusVersion")
    implementation("org.slf4j:slf4j-api")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    sourceCompatibility = JavaVersion.VERSION_26
    targetCompatibility = JavaVersion.VERSION_26
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("fat")
    manifest {
        attributes(mapOf("Application-Verticle" to mainVerticleName))
    }
    mergeServiceFiles()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
    }
}

tasks.withType<JavaExec> {
    args = listOf(mainVerticleName)
}
