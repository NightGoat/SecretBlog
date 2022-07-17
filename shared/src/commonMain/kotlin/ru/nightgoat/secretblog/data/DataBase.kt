package ru.nightgoat.secretblog.data

import io.realm.kotlin.Realm

interface DataBase<T : Entity> {
    val db: Realm
    suspend fun init(): List<T>
    suspend fun add(entity: T): Long
    suspend fun delete(entity: T)
    suspend fun getAll(): List<T>
    suspend fun deleteAll()
    suspend fun update(entity: T)
}