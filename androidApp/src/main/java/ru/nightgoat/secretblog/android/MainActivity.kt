package ru.nightgoat.secretblog.android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.aakira.napier.Napier
import org.koin.android.ext.android.inject
import ru.nightgoat.secretblog.android.presentation.screens.ChatScreen
import ru.nightgoat.secretblog.android.presentation.screens.PinCodeScreen
import ru.nightgoat.secretblog.android.presentation.screens.SettingsScreen
import ru.nightgoat.secretblog.android.presentation.screens.SplashScreen
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: StoreViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.d {
            "App start"
        }
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val state by viewModel.observeState().collectAsState()
            val effects by viewModel.observeSideEffect().collectAsState(initial = BlogEffect.Empty)
            when (effects) {
                is BlogEffect.Navigate -> {
                    LaunchedEffect(effects) {
                        val effect = effects as BlogEffect.Navigate
                        var route = effect.route
                        val argument = effect.argument
                        if (!argument.isNullOrEmpty()) {
                            route = route.plus("/$argument")
                        }
                        navController.navigate(route)
                    }
                }
                is BlogEffect.NavigateBack -> {
                    LaunchedEffect(effects) {
                        navController.popBackStack()
                    }
                }
                is BlogEffect.Toast -> {
                    LaunchedEffect(effects) {
                        Toast.makeText(
                            context,
                            (effects as BlogEffect.Toast).text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            NavHost(navController = navController, startDestination = Screen.Splash.route) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        navController = navController,
                        viewModel = viewModel,
                        state = state,
                        sideEffect = effects
                    )
                }
                composable(
                    Screen.PinCode.route.plus("/{isFromSplash}"),
                    arguments = listOf(navArgument("isFromSplash") { type = NavType.StringType })
                ) { backStackEntry ->
                    PinCodeScreen(
                        navController = navController,
                        viewModel = viewModel,
                        state = state,
                        sideEffect = effects,
                        isFromSplashArg = backStackEntry.arguments?.getString("isFromSplash") ?: "0"
                    )
                }
                composable(Screen.Chat.route) {
                    ChatScreen(
                        navController = navController,
                        viewModel = viewModel,
                        state = state,
                        effects = effects
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        navController = navController,
                        viewModel = viewModel,
                        state = state,
                        sideEffect = effects
                    )
                }
            }
        }
    }
}
