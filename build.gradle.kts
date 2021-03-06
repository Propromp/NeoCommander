import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
    id("com.vanniktech.maven.publish") version "0.18.0"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    create<Copy>("buildPlugin") {
        from(jar)
        into("server/plugins")
    }

    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    val javadocJar by creating(Jar::class) {
        archiveClassifier.set("javadoc")
        from(javadoc, dokkaJavadoc)
    }
    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
    }
}

mavenPublish {
    sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
}
publishing {
    repositories {
        maven {
            url = if(version.toString().endsWith("SNAPSHOT")) {
                uri("$buildDir/repos/snapshots")
            } else {
                uri("$buildDir/repos/releases")
            }
        }
    }
}