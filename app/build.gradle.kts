plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "co.edu.sena.controlparkiq.movil"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "co.edu.sena.controlparkiq.movil"
        minSdk = 24
        targetSdk = 37
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)

    // EV02: consumo de la API REST de ControlPark-IQ
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // EV02: listas y actualización de pantallas
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testImplementation(libs.junit)
    // EV02: servidor simulado para las pruebas unitarias de la capa de datos
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}