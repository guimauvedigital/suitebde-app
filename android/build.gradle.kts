plugins {
    kotlin("android")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.suitebde"
    compileSdk = 34
    defaultConfig {
        applicationId = "me.nathanfallet.suitebde"
        minSdk = 21
        targetSdk = 34
        versionCode = 12
        versionName = "1.0.2"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    flavorDimensions += listOf("env")
    productFlavors {
        create("production") {
            dimension = "env"
        }
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.7.3")
    implementation("androidx.compose.ui:ui-tooling:1.7.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.3")
    implementation("androidx.compose.foundation:foundation:1.7.3")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.3")

    implementation("io.insert-koin:koin-core:3.5.3")
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-messaging")

    implementation("com.google.android.material:material:1.12.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")
}
