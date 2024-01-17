plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.kover")
    id("com.google.devtools.ksp")
    id("app.cash.sqldelight")
    id("com.rickclephas.kmp.nativecoroutines")
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    val coroutinesVersion = "1.7.3"
    val ktorVersion = "2.3.7"
    val koinVersion = "3.5.3"
    val sqlDelightVersion = "2.0.0"

    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                optIn("kotlin.experimental.ExperimentalObjCName")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.sentry:sentry-kotlin-multiplatform:0.2.1")

                implementation("app.cash.sqldelight:runtime:$sqlDelightVersion")
                implementation("co.touchlab:stately-common:2.0.5")

                api("com.rickclephas.kmm:kmm-viewmodel-core:1.0.0-ALPHA-16")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                api("me.nathanfallet.usecases:usecases:1.5.5")
                api("me.nathanfallet.suitebde:suitebde-commons:0.0.5")
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("app.cash.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.mockk:mockk:1.13.8")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
                implementation("app.cash.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "me.nathanfallet.suitebde.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
}

koverReport {
    defaults {
        mergeWith("debug")
    }
}

sqldelight {
    databases {
        create("BdeDatabase") {
            packageName.set("me.nathanfallet.suitebde.database")
        }
    }
}
