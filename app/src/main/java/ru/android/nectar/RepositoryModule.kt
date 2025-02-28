package ru.android.nectar

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.repository.FavouriteRepository
import ru.android.nectar.data.repository.ProductRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideProductRepository(productDao: ProductDao): ProductRepository {
        return ProductRepository(productDao)
    }

    @Provides
    fun provideFavouriteRepository(favouriteDao: FavouriteDao, productDao: ProductDao): FavouriteRepository {
        return FavouriteRepository(favouriteDao, productDao)
    }
}