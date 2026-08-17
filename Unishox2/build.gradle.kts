import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.PublishingExtension

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    namespace = "org.operatorfoundation.unishox2"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        ndk {
            // Specify ABIs you want to build for
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }

        externalNativeBuild {
            cmake {
                cppFlags.add("-std=c++11")
                arguments.addAll(listOf(
                    "-DANDROID_STL=c++_shared"
                ))
            }
        }
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("release") {
            groupId = "org.operatorfoundation"
            artifactId = "unishox2"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Unishox2 Android")
                description.set("Android library implementation of Unishox2 compression")
                url.set("https://github.com/OperatorFoundation/Unishox2Android")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("operatorfoundation")
                        name.set("Operator Foundation")
                        email.set("info@operatorfoundation.org")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/OperatorFoundation/Unishox2Android.git")
                    developerConnection.set("scm:git:ssh://github.com/OperatorFoundation/Unishox2Android.git")
                    url.set("https://github.com/OperatorFoundation/Unishox2Android")
                }
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}