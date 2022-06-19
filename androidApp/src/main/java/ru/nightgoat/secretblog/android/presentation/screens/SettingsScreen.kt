package ru.nightgoat.secretblog.android.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.composables.AppAlert
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
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
        onPincodeCheck = { isChecked ->
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
        onDeleteAllMessagesButton = {
            viewModel.dispatch(SettingsAction.ClearAllMessages)
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
    state: AppState = AppState(),
    dictionary: Dictionary = EnglishDictionary,
    onBackPressed: () -> Unit = {},
    onPincodeCheck: (Boolean) -> Unit = {},
    onDeleteAllMessagesButton: () -> Unit = {}
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
            onPincodeCheck = onPincodeCheck,
            onDeleteAllMessagesButton = onDeleteAllMessagesButton
        )
    }
}

@Composable
private fun Settings(
    state: AppState,
    dictionary: Dictionary,
    onPincodeCheck: (Boolean) -> Unit,
    onDeleteAllMessagesButton: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(defaultPadding)
    ) {
        SettingsCheckBox(dictionary.settingsPincodeOnEnterCheckBox, state, onPincodeCheck)
        SettingsButton(
            text = dictionary.deleteAllMessages,
            imageId = R.drawable.ic_outline_delete_24,
            onClick = onDeleteAllMessagesButton
        )
    }
}

@Composable
private fun SettingsCheckBox(
    text: String,
    state: AppState,
    onCheckBoxClick: (Boolean) -> Unit
) {
    val isPinCodeSet = state.settings.isPinCodeSet
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPadding)
            .clickable {
                onCheckBoxClick(!isPinCodeSet)
            }
    ) {
        Text(
            modifier = Modifier.padding(start = defaultPadding),
            text = text
        )
        Checkbox(
            checked = isPinCodeSet,
            onCheckedChange = onCheckBoxClick
        )
    }
}

@Composable
private fun SettingsButton(
    text: String,
    imageId: Int,
    contentDescription: String = "",
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPadding)
            .clickable {
                onClick()
            }
    ) {
        Text(
            modifier = Modifier.padding(start = defaultPadding),
            text = text
        )
        Image(
            modifier = Modifier.padding(end = 12.dp),
            painter = painterResource(id = imageId),
            contentDescription = contentDescription
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
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isBackButtonVisible) {
                SimpleSpacer()
                Image(
                    modifier = Modifier
                        .clickable {
                            onBackPressed()
                        }
                        .padding(defaultPadding),
                    painter = painterResource(id = R.drawable.ic_outline_arrow_back_24),
                    contentDescription = "go back"
                )
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
    MainContent()
}