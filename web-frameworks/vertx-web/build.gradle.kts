import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.segwarez"
version = "1.0.0"

repositories {
    mavenCentral()
}

val vertxVersion = "4.5.7"
val ongresScramVersion = "2.1"
val micrometerPrometheusVersion = "1.12.5"
val jacksonVersion = "2.17.1"
val lombokVersion = "1.18.32"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.segwarez.vertxweb.ApplicationVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
    mainClass.set(launcherClassName)
}

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-web-validation")
    implementation("io.vertx:vertx-pg-client")
    implementation("com.ongres.scram:client:$ongresScramVersion")
    implementation("io.vertx:vertx-micrometer-metrics")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerPrometheusVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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
    args = listOf("run", mainVerticleName, "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
