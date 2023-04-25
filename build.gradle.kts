val versionBase = property("version.base") as String
val versionBeta = property("version.beta") as String
val jenkinsBuildNumber = System.getenv("BUILD_NUMBER") ?: "Unofficial"

val betaPrefix = if (versionBeta.toBoolean()) "Beta-" else ""
val pluginVersion = "$versionBase.$betaPrefix$jenkinsBuildNumber"

val privateMavenUsername = System.getenv("MAVEN_DEPLOY_USR") ?: property("mavenUsernameSirBlobman")
val privateMavenPassword = System.getenv("MAVEN_DEPLOY_PSW") ?: property("mavenPasswordSirBlobman")

plugins {
    id("java")
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://nexus.sirblobman.xyz/public/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
    compileOnly("com.github.sirblobman.api:core:2.9-dev-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven("https://nexus.sirblobman.xyz/public/") {
            credentials {
                username = privateMavenUsername as String
                password = privateMavenPassword as String
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.sirblobman"
            artifactId = "freeze"
            version = "2.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

tasks {
    named<Jar>("jar") {
        archiveFileName.set("Freeze-$pluginVersion.jar")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"

        val standardOptions = options as StandardJavadocDocletOptions
        standardOptions.addStringOption("Xdoclint:none", "-quiet")
    }

    processResources {
        val pluginName = (findProperty("bukkit.plugin.name") ?: "") as String
        val pluginPrefix = (findProperty("bukkit.plugin.prefix") ?: "") as String
        val pluginDescription = (findProperty("bukkit.plugin.description") ?: "") as String
        val pluginWebsite = (findProperty("bukkit.plugin.website") ?: "") as String
        val pluginMainClass = (findProperty("bukkit.plugin.main") ?: "") as String

        filesMatching("plugin.yml") {
            expand(mapOf(
                "pluginName" to pluginName,
                "pluginPrefix" to pluginPrefix,
                "pluginDescription" to pluginDescription,
                "pluginWebsite" to pluginWebsite,
                "pluginMainClass" to pluginMainClass,
                "pluginVersion" to pluginVersion
            ))
        }
    }
}
