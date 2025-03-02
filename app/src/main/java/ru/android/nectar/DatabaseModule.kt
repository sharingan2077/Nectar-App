package ru.android.nectar

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.android.nectar.data.local.AppDatabase
import ru.android.nectar.data.local.dao.CartDao
import ru.android.nectar.data.local.dao.ExploreDao
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.dao.ProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }

    @Provides
    @Singleton
    fun provideFavouriteDao(appDatabase: AppDatabase): FavouriteDao {
        return appDatabase.favouriteDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(appDatabase: AppDatabase): CartDao {
        return appDatabase.cartDao()
    }

    @Provides
    @Singleton
    fun provideExploreDao(appDatabase: AppDatabase): ExploreDao {
        return appDatabase.exploreDao()
    }
}