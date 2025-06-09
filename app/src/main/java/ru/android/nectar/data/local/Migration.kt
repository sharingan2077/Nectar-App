package ru.android.nectar.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Создание временной таблицы с новым полем
        database.execSQL(
            """

        """
        )

        // Удаление старой таблицы
        database.execSQL("DROP TABLE products")

        // Переименование новой таблицы
        database.execSQL("ALTER TABLE products_new RENAME TO products")
    }
}