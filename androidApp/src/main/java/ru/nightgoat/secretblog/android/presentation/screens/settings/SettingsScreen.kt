package ru.nightgoat.secretblog.android.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.composables.AppAlert
import ru.nightgoat.secretblog.android.presentation.composables.AppIcon
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.android.presentation.screens.settings.composables.SettingsButton
import ru.nightgoat.secretblog.android.presentation.screens.settings.composables.SettingsDropdown
import ru.nightgoat.secretblog.android.presentation.screens.settings.composables.SettingsSwitch
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.BlogAction
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.core.action.SettingsAction
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.EnglishDictionary

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    state: AppState,
    sideEffect: BlogEffect,
    dictionary: Dictionary
) {
    when (sideEffect) {
        is BlogEffect.DeleteAllMessagesDialog -> {
            DeleteAllMesagesDialog(
                dictionary = dictionary,
                onNoClick = {
                    viewModel.clearSideEffects()
                },
                onYesClick = {
                    viewModel.dispatch(BlogAction.ClearDB)
                }
            )
        }
        else -> Unit
    }

    MainContent(
        state = state,
        dictionary = dictionary,
        onBackPressed = {
            viewModel.reduceSideEffect(BlogEffect.NavigateBack)
        },
        onPinOnLoginCheck = { isChecked ->
            if (isChecked) {
                viewModel.dispatch(
                    GlobalAction.Navigate(
                        Screen.PinCode.route,
                        Screen.PinCode.IS_PINCODE_SET
                    )
                )
            } else {
                viewModel.dispatch(SettingsAction.TurnOffPincode)
            }
        },
        onPinSecretVisibilityCheck = { isChecked ->
            val action = if (isChecked) {
                SettingsAction.TurnOnOnSecretVisibilityPin
            } else {
                SettingsAction.TurnOffOnSecretVisibilityPin
            }
            viewModel.dispatch(action)
        },
        onDeleteAllMessagesButton = {
            viewModel.dispatch(SettingsAction.ClearAllMessages)
        },
        onThemeSelect = { themeName ->
            viewModel.dispatch(SettingsAction.SelectTheme(themeName))
        }
    )
}

@Composable
private fun DeleteAllMesagesDialog(
    dictionary: Dictionary,
    onNoClick: () -> Unit,
    onYesClick: () -> Unit
) {
    AppAlert(
        title = dictionary.deleteAllMessagesAlertTitle,
        message = dictionary.deleteAllMessagesAlertMessage,
        leftButtonText = dictionary.no,
        rightButtonText = dictionary.yes,
        onLeftButtonClick = onNoClick,
        onRightButtonClick = onYesClick
    )
}

@Composable
private fun MainContent(
    state: AppState = AppState(
        settings = ru.nightgoat.secretblog.models.Settings(
            isPinCodeSet = true, isPinOnSecretVisibilitySet = true
        )
    ),
    dictionary: Dictionary = EnglishDictionary,
    onBackPressed: () -> Unit = {},
    onPinOnLoginCheck: (Boolean) -> Unit = {},
    onPinSecretVisibilityCheck: (Boolean) -> Unit = {},
    onDeleteAllMessagesButton: () -> Unit = {},
    onThemeSelect: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Toolbar(dictionary.settingsTitle) {
            onBackPressed()
        }
        Settings(
            state = state,
            dictionary = dictionary,
            onPinOnLoginCheck = onPinOnLoginCheck,
            onPinSecretVisibilityCheck = onPinSecretVisibilityCheck,
            onDeleteAllMessagesButton = onDeleteAllMessagesButton,
            onThemeSelect = onThemeSelect
        )
    }
}

@Composable
private fun Settings(
    state: AppState,
    dictionary: Dictionary,
    onPinOnLoginCheck: (Boolean) -> Unit,
    onPinSecretVisibilityCheck: (Boolean) -> Unit,
    onDeleteAllMessagesButton: () -> Unit,
    onThemeSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(defaultPadding)
    ) {
        SettingsSwitch(
            text = dictionary.settingsPincodeOnEnterCheckBox,
            state = state,
            isChecked = state.settings.isPinCodeSet,
            onClick = onPinOnLoginCheck
        )
        AnimatedVisibility(state.settings.isPinCodeSet) {
            SettingsSwitch(
                text = dictionary.settingsPincodeSecretVisibilityCheckBox,
                state = state,
                isChecked = state.settings.isPinOnSecretVisibilitySet,
                onClick = onPinSecretVisibilityCheck
            )
        }
        SettingsDropdown(
            text = "Theme",
            state = state,
            selections = listOf("System", "Dark", "Light"),
            onClick = onThemeSelect
        )
        SettingsButton(
            text = dictionary.deleteAllMessages,
            imageId = R.drawable.ic_outline_delete_24,
            onClick = onDeleteAllMessagesButton
        )
    }
}

@Composable
fun Toolbar(
    title: String = "",
    isBackButtonVisible: Boolean = true,
    onBackPressed: () -> Unit = {},
) {
    Surface(
        elevation = 4.dp,
        color = MaterialTheme.colors.primaryVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(defaultPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isBackButtonVisible) {
                SimpleSpacer()
                AppIcon(
                    drawableId = R.drawable.ic_outline_arrow_back_24,
                    contentDescription = "go back"
                ) {
                    onBackPressed()
                }
            }
            if (title.isNotEmpty()) {
                SimpleSpacer()
                Text(
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    text = title
                )
            }
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
fun SettingsPreview() {
    BlogTheme {
        MainContent()
    }
}