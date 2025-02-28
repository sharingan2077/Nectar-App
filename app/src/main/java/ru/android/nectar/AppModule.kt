package ru.android.nectar

//@Module
//@InstallIn(SingletonComponent::class) // SingletonComponent гарантирует, что объект будет создан один раз
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideAppDatabase(context: Context): AppDatabase {
//        return AppDatabase.getDatabase(context)  // Убедись, что метод getInstance() у тебя правильно реализован
//    }
//
//    @Provides
//    @Singleton
//    fun provideProductRepository(appDatabase: AppDatabase): ProductRepository {
//        return ProductRepository(appDatabase)
//    }
//
//}