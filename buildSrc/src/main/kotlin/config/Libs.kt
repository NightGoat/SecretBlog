package config

object Libs {

    const val kotlin_version = "1.7.20"
    const val koin_version = "3.2.2"
    const val date_time_version = "0.4.0"
    const val coroutines_version = "1.6.4"
    const val coroutines_version_native = "1.6.0-native-mt"
    const val compose_version = "1.2.1"
    const val compose_navigation = "2.5.2"
    const val compose_activity = "1.6.0"
    const val compose_viewmodel = "2.5.1"
    const val realm_db_version = "1.0.2"
    const val kvault_version = "1.9.0"
    const val napierVersion = "2.6.1"
    const val kexVersion = "1.0"

    object Koin {
        const val koin = "io.insert-koin:koin-core:$koin_version"
        const val koin_test = "io.insert-koin:koin-test:$koin_version"

        // Koin main features for Android
        const val koin_android = "io.insert-koin:koin-android:$koin_version"

        // Java Compatibility
        const val koin_java = "io.insert-koin:koin-android-compat:$koin_version"

        // Jetpack WorkManager
        const val koin_workmanager = "io.insert-koin:koin-androidx-workmanager:$koin_version"

        // Navigation Graph
        const val koin_nav = "io.insert-koin:koin-androidx-navigation:$koin_version"

        // Jetpack Compose
        const val koin_compose = "io.insert-koin:koin-androidx-compose:$koin_version"

        // Koin for Ktor
        const val koin_ktor = "io.insert-koin:koin-ktor:$koin_version"

        // SLF4J Logger
        const val koin_slf4j = "io.insert-koin:koin-logger-slf4j:$koin_version"
    }

    object Core {
        const val date_time = "org.jetbrains.kotlinx:kotlinx-datetime:$date_time_version"
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
        const val coroutines_android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
        const val realm_db = "io.realm.kotlin:library-base:$realm_db_version"
        const val kvault = "com.liftric:kvault:$kvault_version"
        const val napier = "io.github.aakira:napier:$napierVersion"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:$compose_version"

        // Tooling support (Previews, etc.)
        const val ui_tooling = "androidx.compose.ui:ui-tooling:$compose_version"

        // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
        const val foundation = "androidx.compose.foundation:foundation:$compose_version"

        // Material Design
        const val material = "androidx.compose.material:material:$compose_version"

        // Material design icons
        const val icons = "androidx.compose.material:material-icons-core:$compose_version"
        const val icons_extended =
            "androidx.compose.material:material-icons-extended:$compose_version"

        // UI Tests
        const val ui_tests = "androidx.compose.ui:ui-test-junit4:$compose_version"

        const val activity = "androidx.activity:activity-compose:$compose_activity"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:$compose_viewmodel"
        const val navigation = "androidx.navigation:navigation-compose:$compose_navigation"
    }

    object Other {
        const val kex = "io.github.nightgoat:Kextensions-core:$kexVersion"
    }
}