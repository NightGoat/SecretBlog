package ru.nightgoat.secretblog.providers

import com.liftric.kvault.KVault
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class KVaultPref {

    abstract val kvault: KVault

    fun stringPref(key: String) = readWriteProp(
        defaultValue = null,
        getValue = {
            kvault.string(key)
        },
        setValue = { value ->
            value?.let {
                kvault.set(key, it)
            }
        }
    )

    fun booleanPref(key: String, defaultValue: Boolean = false) = readWriteProp(
        defaultValue = defaultValue,
        getValue = {
            kvault.bool(key)
        },
        setValue = { value ->
            kvault.set(key, value)
        }
    )

    private fun <T> readWriteProp(
        defaultValue: T,
        getValue: () -> T?,
        setValue: (T) -> Unit
    ): ReadWriteProperty<KVaultPref, T> {
        return object : ReadWriteProperty<KVaultPref, T> {
            override fun getValue(thisRef: KVaultPref, property: KProperty<*>): T {
                return getValue() ?: defaultValue
            }

            override fun setValue(thisRef: KVaultPref, property: KProperty<*>, value: T) {
                setValue(value)
            }
        }
    }
}