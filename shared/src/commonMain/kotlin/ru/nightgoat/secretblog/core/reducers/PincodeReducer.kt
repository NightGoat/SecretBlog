package ru.nightgoat.secretblog.core.reducers

import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.PinCodeAction
import ru.nightgoat.secretblog.core.setPincode

fun StoreViewModel.pincodeReducer(action: PinCodeAction, oldState: AppState) {
    when (action) {
        is PinCodeAction.CheckPincode -> {
            launch {
                val isPincodeCorrect = settingsProvider.isPinCodeCorrect(action.pincodeToCheck)
                val effect = BlogEffect.PincodeCheckResult(isPincodeCorrect)
                sideEffect.emit(effect)
                if (!isPincodeCorrect) {
                    sideEffect.emit(BlogEffect.Toast("Wrong pincode!"))
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
    }
}