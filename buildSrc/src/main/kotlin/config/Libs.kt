package config


object Libs {

    const val koin_version = "3.2.0-beta-1"
    const val kodein_db_version = "0.8.1-beta"
    const val redux_version = "0.5.5"
    const val date_time_version = "0.3.2"
    const val coroutines_version = "1.6.1"
    const val compose_version = "1.1.1"

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

    object KodeinDb {
        const val kodein_db = "org.kodein.db:kodein-db:$kodein_db_version"
        const val kotlin_serializer =
            "org.kodein.db:kodein-db-serializer-kotlinx:$kodein_db_version"
    }

    object Core {
        const val redux = "org.reduxkotlin:redux-kotlin-threadsafe:$redux_version"
        const val date_time = "org.jetbrains.kotlinx:kotlinx-datetime:$date_time_version"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
        const val coroutines_android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
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
    }
}