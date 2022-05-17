package ru.nightgoat.secretblog.core

fun StoreViewModel.watchState() = observeState().wrap()
fun StoreViewModel.watchSideEffect() = observeSideEffect().wrap()