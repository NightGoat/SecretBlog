package ru.nightgoat.secretblog.providers

import com.liftric.kvault.KVault

expect class KvaultProvider() {
    fun provide(): KVault
}