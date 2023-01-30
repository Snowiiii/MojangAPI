plugins {
    `java-library`
    id("de.chojo.publishdata") version "1.0.9"
    `maven-publish`
}

group = "de.snowii"
version = "1.0.0"
description = "Simple & User Friendly Java Mojang API"

repositories {
    mavenCentral()
}

publishData {
    useEldoNexusRepos(useMain = true)
    publishComponent("java")
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains:annotations:24.0.0")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}


publishing {
    publications.create<MavenPublication>("maven") {
        // Configure our maven publication
        publishData.configurePublication(this)
    }
    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    // Those credentials need to be set under "Settings -> Secrets -> Actions" in your repository
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            name = "Mojang-API"
            setUrl(publishData.getRepository())
        }
    }
}


