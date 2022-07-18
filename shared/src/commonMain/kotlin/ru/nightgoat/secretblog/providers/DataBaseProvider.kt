package ru.nightgoat.secretblog.providers

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.migration.AutomaticSchemaMigration
import ru.nightgoat.secretblog.models.BlogMessage

class DataBaseProvider {
    private val config by lazy {
        RealmConfiguration.Builder(schema = setOf(BlogMessage::class))
            .schemaVersion(1)
            .migration(
                AutoMigration()
            )
            .build()
    }

    val db: Realm by lazy {
        Realm.open(config)
    }
}

private class AutoMigration : AutomaticSchemaMigration {
    override fun migrate(migrationContext: AutomaticSchemaMigration.MigrationContext) {

    }
}