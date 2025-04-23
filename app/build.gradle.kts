plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.chatranslate"
    compileSdk = 35


    defaultConfig {
        applicationId = "com.example.chatranslate"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core AndroidX libraries
    implementation(libs.androidx.core.ktx) // Core KTX library
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") // Correct version



    // Material Design libraries
    implementation(libs.androidx.material3) // Material Design 3 (modern)
    implementation("com.google.android.material:material:1.11.0") // Older Material Design (optional, if needed)

    // Compose (if applicable)
    implementation(platform(libs.androidx.compose.bom)) // Compose BOM
    implementation(libs.androidx.activity.compose) // Compose Activity
    implementation(libs.androidx.ui.tooling.preview) // UI Previews
    implementation(libs.androidx.material3) // Material 3 for Compose

    // Test libraries
    testImplementation(libs.junit) // JUnit for unit testing
    androidTestImplementation(libs.androidx.junit) // AndroidX JUnit
    androidTestImplementation(libs.androidx.espresso.core) // Espresso Core
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Compose Testing BOM
    androidTestImplementation(libs.androidx.ui.test.junit4) // Compose JUnit Tests

    // Debugging tools
    debugImplementation(libs.androidx.ui.tooling) // Compose UI Tooling
    debugImplementation(libs.androidx.ui.test.manifest) // Test manifest
}
