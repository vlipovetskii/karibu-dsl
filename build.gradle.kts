import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.gretty") version "3.0.3"
    `maven-publish`
    id("com.vaadin") version "0.14.3.7" apply(false)
    signing
}

defaultTasks("clean", "build")

allprojects {
    group = "com.github.mvysny.karibudsl"
    version = "1.0.5-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { setUrl("https://maven.vaadin.com/vaadin-prereleases/") }
    }

    tasks {
        // Heroku
        val stage by registering {
            // see example-v8/build.gradle.kts - the 'stage' task is properly configured there
        }
    }
}

subprojects {

    apply {
        plugin("maven-publish")
        plugin("kotlin")
        plugin("org.gradle.signing")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.useIR = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            // to see the exceptions of failed tests in CI console.
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    // creates a reusable function which configures proper deployment to Maven Central
    ext["configureBintray"] = { artifactId: String ->

        java {
            withJavadocJar()
            withSourcesJar()
        }

        tasks.withType<Javadoc> {
            isFailOnError = false
        }

        publishing {
            repositories {
                maven {
                    setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = project.properties["ossrhUsername"] as String? ?: "Unknown user"
                        password = project.properties["ossrhPassword"] as String? ?: "Unknown user"
                    }
                }
            }
            publications {
                create("mavenJava", MavenPublication::class.java).apply {
                    groupId = project.group.toString()
                    this.artifactId = artifactId
                    version = project.version.toString()
                    pom {
                        description.set("Karibu-DSL, Kotlin extensions/DSL for Vaadin")
                        name.set(artifactId)
                        url.set("https://github.com/mvysny/karibu-dsl")
                        licenses {
                            license {
                                name.set("The MIT License (MIT)")
                                url.set("https://opensource.org/licenses/MIT")
                                distribution.set("repo")
                            }
                        }
                        developers {
                            developer {
                                id.set("mavi")
                                name.set("Martin Vysny")
                                email.set("martin@vysny.me")
                            }
                        }
                        scm {
                            url.set("https://github.com/mvysny/karibu-dsl")
                        }
                    }

                    from(components["java"])
                }
            }
        }

        signing {
            sign(publishing.publications["mavenJava"])
        }
    }
}
