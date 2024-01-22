plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "tw.tonyyang.englishwords"
    compileSdk = 34

    defaultConfig {
        applicationId = "tw.tonyyang.englishwords"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "0.2.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        multiDexEnabled = true
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
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val roomVersion = "2.5.0"
    val constraintLayoutVersion = "2.0.4"
    val recyclerviewVersion = "1.2.1"
    val coroutinesCoreVersion = "1.3.9"
    val coroutinesAndroidVersion = "1.3.9"
    val materialVersion = "1.4.0"
    val appcompatVersion = "1.3.0"
    val junitVersion = "4.13.2"
    val gsonVersion = "2.8.6"
    val okhttpVersion = "4.9.1"
    val ktxCoreVersion = "1.6.0"
    val ktxFragmentVersion = "1.3.5"
    val ktxViewModelVersion = "2.3.1"
    val koinVersion = "2.2.2"
    val viewpager2Version = "1.0.0"
    val materialDialogVersion = "2.2.2"
    val timberVersion = "5.0.1"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCoreVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesAndroidVersion")

    // Android components
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerviewVersion")
    implementation("androidx.viewpager2:viewpager2:$viewpager2Version")
    implementation("androidx.core:core-ktx:$ktxCoreVersion")
    implementation("androidx.fragment:fragment-ktx:$ktxFragmentVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$ktxViewModelVersion")
    implementation("com.google.android.material:material:$materialVersion")

    // Room components
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    // Testing
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("junit:junit:$junitVersion")

    // 3rd party libs
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("dev.shreyaspatil.MaterialDialog:MaterialDialog:$materialDialogVersion")

    // koin
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-scope:$koinVersion")
    implementation("io.insert-koin:koin-androidx-viewmodel:$koinVersion")
    implementation("io.insert-koin:koin-androidx-fragment:$koinVersion")

    implementation("com.jakewharton.timber:timber:$timberVersion")
}