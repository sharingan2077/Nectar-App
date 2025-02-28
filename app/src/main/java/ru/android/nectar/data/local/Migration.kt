package ru.android.nectar.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Создание временной таблицы с новым полем
        database.execSQL(
            """
            CREATE TABLE products_new (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                imageRes INTEGER NOT NULL,
                name TEXT NOT NULL,
                spec TEXT NOT NULL,
                price TEXT NOT NULL,
                category TEXT NOT NULL,
                productType TEXT NOT NULL
                            )
        """
        )

        // Удаление старой таблицы
        database.execSQL("DROP TABLE products")

        // Переименование новой таблицы
        database.execSQL("ALTER TABLE products_new RENAME TO products")
    }
}