plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
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
            isStatic = true
        }
    }

    val coroutinesVersion = "1.8.1"
    val ktorVersion = "2.3.11"
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
                implementation("io.sentry:sentry-kotlin-multiplatform:0.7.1")

                implementation("app.cash.sqldelight:runtime:$sqlDelightVersion")
                implementation("co.touchlab:stately-common:2.0.5")

                api("com.rickclephas.kmp:kmp-observableviewmodel-core:1.0.0-BETA-1-kotlin-2.0.0-RC2")
                api("me.nathanfallet.usecases:usecases:1.6.1")
                api("me.nathanfallet.suitebde:suitebde-commons:0.0.18")
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
                implementation("io.mockk:mockk:1.13.11")
                implementation("app.cash.sqldelight:sqlite-driver:$sqlDelightVersion")
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
                implementation("me.nathanfallet.myapps:myapps-ios:1.3.1")
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

kover {
    currentProject {
        createVariant("custom") {
            addWithDependencies("debug")
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("me.nathanfallet.suitebde.database")
        }
    }
}
