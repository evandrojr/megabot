import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    kotlin("jvm") version "1.6.21"
    kotlin("jvm") version "1.7.0"
    application
}

group = "org.evandro"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.3.0")
    implementation("com.github.thomasnield:kotlin-statistics:-SNAPSHOT")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.jayway.jsonpath:json-path:2.4.0")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")

//    jfreechart
    implementation("org.apache.commons:commons-lang3:3.7")
    implementation("commons-io:commons-io:2.6")
    implementation("org.jfree:jcommon:1.0.24")
    implementation("org.jfree:jfreechart:1.5.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}

application {
    mainClass.set("org.evandro.megabot.MainKt")
}


tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}