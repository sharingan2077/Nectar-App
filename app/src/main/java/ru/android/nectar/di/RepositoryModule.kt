package ru.android.nectar.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.android.nectar.data.local.dao.CartDao
import ru.android.nectar.data.local.dao.ExploreDao
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.repository.CartRepository
import ru.android.nectar.data.repository.ExploreRepository
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

    @Provides
    fun provideCartRepository(cartDao: CartDao, productDao: ProductDao): CartRepository {
        return CartRepository(cartDao, productDao)
    }

    @Provides
    fun provideExploreRepository(exploreDao: ExploreDao): ExploreRepository {
        return ExploreRepository(exploreDao)
    }

}