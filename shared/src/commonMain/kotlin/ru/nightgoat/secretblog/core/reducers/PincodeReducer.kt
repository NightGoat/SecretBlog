package ru.nightgoat.secretblog.core.reducers

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.PinCodeAction
import ru.nightgoat.secretblog.core.setPincode
import ru.nightgoat.secretblog.utils.GlobalConstants.PIN_CODE_DROP_TO_EMPTY_DELAY

fun StoreViewModel.pincodeReducer(action: PinCodeAction, oldState: AppState) {
    when (action) {
        is PinCodeAction.CheckPincode -> {
            launch {
                val isPincodeCorrect = settingsProvider.isPinCodeCorrect(action.pincodeToCheck)
                val effect = BlogEffect.PincodeCheckResult(isPincodeCorrect)
                reduceSideEffect(effect)
                if (!isPincodeCorrect) {
                    sideEffect.emit(BlogEffect.Toast("Wrong pincode!"))
                    delay(PIN_CODE_DROP_TO_EMPTY_DELAY)
                    reduceSideEffect(BlogEffect.DropEnteredPincodeToEmpty)
                }
            }
        }
        is PinCodeAction.SetPincode -> {
            launch {
                settingsProvider.setNewPincode(action.newPincode)
                state.value = oldState.setPincode(true)
                goBack()
            }
        }
        is PinCodeAction.CannotRememberPinCode -> {
            launch {
                sideEffect.emit(BlogEffect.CannotRememberPinCodeDialog)
            }
        }
        is PinCodeAction.ReverseSecretMessagesVisibility -> {
            launch {
                state.value = oldState.reversedVisibility
                reduceSideEffect(BlogEffect.NavigateBack)
            }
        }
    }
}