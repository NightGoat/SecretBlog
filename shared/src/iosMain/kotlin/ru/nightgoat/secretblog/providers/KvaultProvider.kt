package ru.nightgoat.secretblog.providers

import com.liftric.kvault.KVault

actual class KvaultProvider {
    actual fun provide(): KVault {
        return KVault()
    }
}