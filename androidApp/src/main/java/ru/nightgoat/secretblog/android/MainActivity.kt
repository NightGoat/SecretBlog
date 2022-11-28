package ru.nightgoat.secretblog.android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.aakira.napier.Napier
import org.koin.android.ext.android.inject
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.screens.SplashScreen
import ru.nightgoat.secretblog.android.presentation.screens.chat.ChatScreen
import ru.nightgoat.secretblog.android.presentation.screens.pincode.PinCodeScreen
import ru.nightgoat.secretblog.android.presentation.screens.settings.SettingsScreen
import ru.nightgoat.secretblog.android.presentation.util.Twitter
import ru.nightgoat.secretblog.android.presentation.util.launch
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.Screen
import ru.nightgoat.secretblog.core.Screen.PinCode.IS_PINCODE_CHECK_ARG
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.StringProvider


class MainActivity : AppCompatActivity() {
    private val viewModel: StoreViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.d {
            "App start"
        }
        val dictionary = StringProvider().provide()
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val state by viewModel.observeState().collectAsState()
            val effects by viewModel.observeSideEffect().collectAsState(initial = BlogEffect.Empty)
            when (effects) {
                is BlogEffect.LogOut -> {
                    effects.launch {
                        navController.navigate(Screen.PinCode.routeWithCheck)
                    }
                }
                is BlogEffect.Navigate -> {
                    effects.launch {
                        val effect = effects as BlogEffect.Navigate
                        var route = effect.route
                        val argument = effect.argument
                        if (!argument.isNullOrEmpty()) {
                            route = route.plus("/$argument")
                        }
                        navController.navigate(route) {
                            if (effect.clearCurrentScreenFromBackStack) {
                                popUpTo(Screen.Chat.route)
                            }
                        }
                    }
                }
                is BlogEffect.NavigateBack -> {
                    effects.launch {
                        navController.popBackStack()
                    }
                }
                is BlogEffect.Toast -> {
                    effects.launch {
                        Toast.makeText(
                            context,
                            (effects as BlogEffect.Toast).text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is BlogEffect.ClearBackStackAndGoToChat -> {
                    effects.launch {
                        navController.navigate(Screen.Chat.route) {
                            popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                }
                is BlogEffect.Twitter -> {
                    effects.launch {
                        val effect = effects as BlogEffect.Twitter
                        val intent = Twitter.getIntent(context, effect.text)
                        startActivity(intent)
                    }
                }
                is BlogEffect.ClearPincodeFromBackStack -> {
                    effects.launch {
                        navController.clearBackStack(Screen.PinCode.route)
                    }
                }
            }
            BlogTheme(
                selectedTheme = state.settings.themeType
            ) {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    AppNavigationGraph(navController, state, effects, dictionary)
                }
            }
        }
    }

    @Composable
    private fun AppNavigationGraph(
        navController: NavHostController,
        state: AppState,
        effects: BlogEffect,
        dictionary: Dictionary
    ) {
        NavHost(navController = navController, startDestination = Screen.Splash.route) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    navController = navController,
                    viewModel = viewModel,
                    state = state,
                    sideEffect = effects,
                    dictionary = dictionary
                )
            }
            composable(
                Screen.PinCode.route.plus("/{$IS_PINCODE_CHECK_ARG}"),
                arguments = listOf(navArgument(IS_PINCODE_CHECK_ARG) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                PinCodeScreen(
                    navController = navController,
                    viewModel = viewModel,
                    state = state,
                    sideEffect = effects,
                    isPincodeCheckArg = backStackEntry.arguments?.getString(IS_PINCODE_CHECK_ARG)
                        ?: Screen.PinCode.IS_PINCODE_SET,
                    dictionary = dictionary
                )
            }
            composable(Screen.Chat.route) {
                ChatScreen(
                    navController = navController,
                    viewModel = viewModel,
                    state = state,
                    effects = effects,
                    dictionary = dictionary
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    navController = navController,
                    viewModel = viewModel,
                    state = state,
                    sideEffect = effects,
                    dictionary = dictionary
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispatch(GlobalAction.AppPaused)
    }

    override fun onResume() {
        super.onResume()
        viewModel.dispatch(GlobalAction.AppResumed)
    }
}
