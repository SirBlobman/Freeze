import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val apiVersion = fetchProperty("version.api", "invalid")
val mavenUsername = fetchEnv("MAVEN_DEPLOY_USR", "maven.username.sirblobman", "")
val mavenPassword = fetchEnv("MAVEN_DEPLOY_PSW", "maven.password.sirblobman", "")

val baseVersion = fetchProperty("version.base", "invalid")
val betaString = fetchProperty("version.beta", "false")
val jenkinsBuildNumber = fetchEnv("BUILD_NUMBER", null, "Unofficial")

val betaBoolean = betaString.toBoolean()
val betaVersion = if (betaBoolean) "Beta-" else ""
version = "$baseVersion.$betaVersion$jenkinsBuildNumber"

fun fetchProperty(propertyName: String, defaultValue: String): String {
    val found = findProperty(propertyName)
    if (found != null) {
        return found.toString()
    }

    return defaultValue
}

fun fetchEnv(envName: String, propertyName: String?, defaultValue: String): String {
    val found = System.getenv(envName)
    if (found != null) {
        return found
    }

    if (propertyName != null) {
        return fetchProperty(propertyName, defaultValue)
    }

    return defaultValue
}

plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.3.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://nexus.sirblobman.xyz/public/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.1.0") // JetBrains Annotations
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+") // PaperMC API
    implementation("org.bstats:bstats-bukkit:3.2.1") // bStats Bukkit
    implementation("com.github.sirblobman.api:folia-helper:1.0.2-SNAPSHOT") // Folia Helper
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven("https://nexus.sirblobman.xyz/public/") {
            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.sirblobman"
            artifactId = "freeze"
            version = apiVersion
            from(components["shadow"])
        }
    }
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("Freeze")
        archiveClassifier.set(null)
        relocate("org.bstats", "com.github.sirblobman.freeze.bstats")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
        val standardOptions = options as StandardJavadocDocletOptions
        standardOptions.addStringOption("Xdoclint:none", "-quiet")
    }

    named<ProcessResources>("processResources") {
        val pluginVersion = providers.provider { project.version.toString() }
        inputs.property("version", pluginVersion)

        filesMatching("paper-plugin.yml") {
            expand(mapOf("version" to pluginVersion.get()))
        }
    }
}
