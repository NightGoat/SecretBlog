package ru.nightgoat.secretblog.data

import io.realm.kotlin.Realm

interface DataBase<T : Entity> {
    val db: Realm
    suspend fun add(entity: T)
    suspend fun delete(entity: T)
    suspend fun getAll(): List<T>
    suspend fun deleteAll()
}