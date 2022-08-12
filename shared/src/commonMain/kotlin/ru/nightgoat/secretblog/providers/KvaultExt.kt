package ru.nightgoat.secretblog.providers

import com.liftric.kvault.KVault
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun KVault.stringPref(key: String? = null, defaultValue: String = "") = readWriteProp(
    defaultValue = defaultValue,
    getValue = { prop ->
        val newKey = key ?: prop.name
        this.string(newKey)
    },
    setValue = { prop, value ->
        val newKey = key ?: prop.name
        this.set(newKey, value)
    }
)

fun KVault.stringPrefNullable(key: String? = null, defaultValue: String? = null) =
    readWritePropNullable(
        defaultValue = defaultValue,
        getValue = { prop ->
            val newKey = key ?: prop.name
            this.string(newKey)
        },
        setValue = { prop, value ->
            val newKey = key ?: prop.name
            this.set(newKey, value)
        }
    )

fun KVault.booleanPref(key: String? = null, defaultValue: Boolean = false) = readWriteProp(
    defaultValue = defaultValue,
    getValue = { prop ->
        val newKey = key ?: prop.name
        this.bool(newKey)
    },
    setValue = { prop, value ->
        val newKey = key ?: prop.name
        this.set(newKey, value)
    }
)

fun KVault.booleanPrefNullable(key: String? = null, defaultValue: Boolean? = null) =
    readWritePropNullable(
        defaultValue = defaultValue,
        getValue = { prop ->
            val newKey = key ?: prop.name
            this.bool(newKey)
        },
        setValue = { prop, value ->
            val newKey = key ?: prop.name
            this.set(newKey, value)
        }
    )

fun KVault.longPref(key: String? = null, defaultValue: Long = 0) = readWriteProp(
    defaultValue = defaultValue,
    getValue = { prop ->
        val newKey = key ?: prop.name
        this.long(newKey)
    },
    setValue = { prop, value ->
        val newKey = key ?: prop.name
        this.set(newKey, value)
    }
)

fun KVault.longPrefNullable(key: String? = null, defaultValue: Long? = null) =
    readWritePropNullable(
        defaultValue = defaultValue,
        getValue = { prop ->
            val newKey = key ?: prop.name
            this.long(newKey)
        },
        setValue = { prop, value ->
            val newKey = key ?: prop.name
            this.set(newKey, value)
        }
    )

fun <S> KVault.intPref(key: String? = null, defaultValue: Int = 0) = readWriteProp(
    defaultValue = defaultValue,
    getValue = { prop ->
        val newKey = key ?: prop.name
        this.int(newKey)
    },
    setValue = { prop, value ->
        val newKey = key ?: prop.name
        this.set(newKey, value)
    }
)

fun <S> KVault.intPrefNullable(key: String? = null, defaultValue: Int? = null) =
    readWritePropNullable(
        defaultValue = defaultValue,
        getValue = { prop ->
            val newKey = key ?: prop.name
            this.int(newKey)
        },
        setValue = { prop, value ->
            val newKey = key ?: prop.name
            this.set(newKey, value)
        }
    )

fun <S> KVault.floatPref(key: String? = null, defaultValue: Float = 0.0f) = readWriteProp(
    defaultValue = defaultValue,
    getValue = { prop ->
        val newKey = key ?: prop.name
        this.float(newKey)
    },
    setValue = { prop, value ->
        val newKey = key ?: prop.name
        this.set(newKey, value)
    }
)

fun <S> KVault.floatPrefNullable(key: String? = null, defaultValue: Float? = null) =
    readWritePropNullable(
        defaultValue = defaultValue,
        getValue = { prop ->
            val newKey = key ?: prop.name
            this.float(newKey)
        },
        setValue = { prop, value ->
            val newKey = key ?: prop.name
            this.set(newKey, value)
        }
    )

fun KVault.doublePref(key: String? = null, defaultValue: Double = 0.0) = readWriteProp(
    defaultValue = defaultValue,
    getValue = { prop ->
        val newKey = key ?: prop.name
        this.double(newKey)
    },
    setValue = { prop, value ->
        val newKey = key ?: prop.name
        this.set(newKey, value)
    }
)

fun KVault.doublePrefNullable(key: String? = null, defaultValue: Double? = null) =
    readWritePropNullable(
        defaultValue = defaultValue,
        getValue = { prop ->
            val newKey = key ?: prop.name
            this.double(newKey)
        },
        setValue = { prop, value ->
            val newKey = key ?: prop.name
            this.set(newKey, value)
        }
    )

fun KVault.exists(key: String? = null) = readProp(
    defaultValue = false,
    getValue = { prop ->
        val newKey = key ?: prop.name
        this.existsObject(newKey)
    }
)

private fun <T> readProp(
    defaultValue: T,
    getValue: (KProperty<*>) -> T?,
): ReadOnlyProperty<Any, T> {
    return ReadOnlyProperty { _, property -> getValue(property) ?: defaultValue }
}

private fun <T> readWriteProp(
    defaultValue: T,
    getValue: (KProperty<*>) -> T?,
    setValue: (KProperty<*>, T) -> Unit
): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getValue(property) ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            setValue(property, value)
        }
    }
}

private fun <T> readWritePropNullable(
    defaultValue: T?,
    getValue: (KProperty<*>) -> T?,
    setValue: (KProperty<*>, T) -> Unit
): ReadWriteProperty<Any, T?> {
    return object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return getValue(property) ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            value?.let {
                setValue(property, it)
            }
        }
    }
}