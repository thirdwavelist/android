import org.gradle.api.JavaVersion

object Config {
    const val minSdk = 23
    const val compileSdk = 30
    const val targetSdk = 30
    val javaVersion = JavaVersion.VERSION_1_8
}

object Versions {
    // Jetbrains
    const val coroutines = "1.4.2"

    // Airbnb
    const val lottie = "3.6.0"

    // Google
    const val google_services = "4.3.5"
    const val hilt = "2.31.2-alpha"
    const val gson = "2.8.6"

    // Google Firebase
    const val crashlytics_gradle = "2.4.1"
    const val firebase_bom = "26.4.0"

    // Google - Jetpack
    const val nav_components = "2.3.3"
    const val appcompat = "1.2.0"
    const val constraint_layout = "2.0.4"
    const val material_design = "1.2.1"
    const val recycler_view = "1.1.0"
    const val card_view = "1.0.0"
    const val core_ktx = "1.3.2"
    const val fragment_ktx = "1.2.5"
    const val room = "2.2.6"
    const val lifecycle = "2.2.0"
    const val espresso = "3.3.0"
    const val test_ext_junit = "1.1.2"
    const val test_jetpack_core = "1.3.0"
    const val test_jetpack_runner = "1.3.0"
    const val arch_core = "2.1.0"

    // Bump Technologies
    const val glide = "4.12.0"

    // Square
    const val okhttp = "4.7.2"
    const val retrofit = "2.7.0"
    const val burst = "1.2.0"

    // haroldadmin
    const val network_adapter = "4.1.0"

    // JUnit Org
    const val junit = "4.13.1"

    // AssertJ
    const val assertJ = "3.19.0"

    // Mockito
    const val mockito_kotlin = "2.2.0"
    const val mockito_inline = "3.1.0"

    // JakeWharton
    const val timber = "4.7.1"
}