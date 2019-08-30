

object This {
    val version = "1.2.2"
    val groupId = "com.github.testng-team"
    val artifactId = "reportng"
    val description = "A TestNG reporter plugin"
    val url = "https://testng.org"
    val scm = "github.com/testng-team/reportng"

    // Should not need to change anything below
    val issueManagementUrl = "https://$scm/issues"
    val isSnapshot = version.contains("SNAPSHOT")
}

allprojects {
    group = This.groupId
    version = This.version
    apply<MavenPublishPlugin>()
    tasks.withType<Javadoc> {
        options {
            quiet()
//            outputLevel = JavadocOutputLevel.QUIET
//            jFlags = listOf("-Xdoclint:none", "foo")
//            "-quiet"
        }
    }
}

buildscript {
    repositories {
        jcenter()
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2") }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://plugins.gradle.org/m2") }
}

plugins {
    java
    maven
    `java-library`
    `maven-publish`
    signing
    id("com.jfrog.bintray") version "1.8.3" // Don't use 1.8.4, crash when publishing
}

dependencies {
    listOf("org.testng:testng:7.0.0", "org.freemarker:freemarker:2.3.28", "com.google.inject:guice:4.2.0")
            .forEach { compile(it) }
    listOf("org.testng:testng:7.0.0").forEach { testCompile(it) }
}

//
// Releases:
// ./gradlew bintrayUpload (to JCenter)
// ./gradlew publish (to Sonatype, then go to https://oss.sonatype.org/index.html#stagingRepositories to publish)
//

bintray {
    user = project.findProperty("bintrayUser")?.toString()
    key = project.findProperty("bintrayApiKey")?.toString()
    dryRun = false
    publish = false

    setPublications("custom")

    with(pkg) {
        repo = "maven"
        name = This.artifactId
        with(version) {
            name = This.version
            desc = This.description
            with(gpg) {
                sign = true
            }
        }
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

with(publishing) {
    publications {
        create<MavenPublication>("custom") {
            groupId = This.groupId
            artifactId = This.artifactId
            version = project.version.toString()
            afterEvaluate {
                from(components["java"])
            }
            artifact(sourcesJar)
            artifact(javadocJar)
            pom {
                name.set(This.artifactId)
                description.set(This.description)
                url.set(This.url)
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set(This.issueManagementUrl)
                }
                developers {
                    developer {
                        id.set("cbeust")
                        name.set("Cedric Beust")
                        email.set("cedric@beust.com")
                    }
                    developer {
                        name.set("Julien Herr")
                    }
                    developer {
                        name.set("Krishnan Mahadevan")
                    }
                }
                scm {
                    connection.set("scm:git:git://${This.scm}.git")
                    url.set("https://${This.scm}")
                }
            }
        }
    }

    repositories {
        mavenLocal()
        maven {
            name = "sonatype"
            url = if (This.isSnapshot)
                uri("https://oss.sonatype.org/content/repositories/snapshots/") else
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("sonatypeUser") as? String
                password = project.findProperty("sonatypePassword") as? String
            }
        }
        maven {
            name = "myRepo"
            url = uri("file://$buildDir/repo")
        }
    }
}

tasks.register("publishSnapshotOnly") {
    if (This.isSnapshot) {
        println("Publishing ${This.version}")
        dependsOn("publish")
    } else {
        println("Not a snapshot, not publishing anything")
    }
}

with(signing) {
    sign(publishing.publications.getByName("custom"))
}

tasks.test {
    useTestNG() {
        suites("src/test/resources/testng.xml")
    }
}

tasks["publish"].doLast {
    if (! This.isSnapshot) {
        println("Now go to https://oss.sonatype.org/index.html#stagingRepositories to close" +
                " and publish the distribution")
    }
}