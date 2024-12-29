plugins {
    `java-library`
    id("de.chojo.publishdata") version "1.2.5"
    `maven-publish`
}

group = "de.snowii"
version = "1.1.0"
description = "Simple & User Friendly Java Mojang API"

repositories {
    mavenCentral()
}

publishData {
    useEldoNexusRepos(dev = false)
    publishComponent("java")
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.jetbrains:annotations:26.0.1")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

java{
    toolchain{
        languageVersion.set(JavaLanguageVersion.of(17))
    }
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

            name = "EldoNexus"
            setUrl(publishData.getRepository())
        }
    }
}


