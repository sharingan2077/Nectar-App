package ru.android.nectar.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.android.nectar.data.api.AuthApi
import ru.android.nectar.data.api.OrderApi
import ru.android.nectar.data.api.ProductApiService
import ru.android.nectar.data.api.ProductCartApi
import ru.android.nectar.data.api.ProductFavoriteApi
import ru.android.nectar.data.local.dao.CartDao
import ru.android.nectar.data.local.dao.ExploreDao
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.dao.OrderDao
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.dao.SearchHistoryDao
import ru.android.nectar.data.repository.AuthRepository
import ru.android.nectar.data.repository.CartRepository
import ru.android.nectar.data.repository.ExploreRepository
import ru.android.nectar.data.repository.FavouriteRepository
import ru.android.nectar.data.repository.OrderRepository
import ru.android.nectar.data.repository.ProductRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideProductRepository(
        productDao: ProductDao,
        productApiService: ProductApiService,
        @ApplicationContext context: Context
    ): ProductRepository {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ProductRepository(productDao, productApiService, connectivityManager, context)
    }

    @Provides
    fun provideAuthRepository(
        api: AuthApi,
        favouriteApi: ProductFavoriteApi,
        cartApi: ProductCartApi,
        favoriteDao: FavouriteDao,
        cartDao: CartDao,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(api, favouriteApi, cartApi, favoriteDao, cartDao, context)
    }

    @Provides
    fun provideFavouriteRepository(
        favouriteApi: ProductFavoriteApi,
        favouriteDao: FavouriteDao,
        productDao: ProductDao,
        @ApplicationContext context: Context
    ): FavouriteRepository {
        return FavouriteRepository(favouriteApi, favouriteDao, productDao, context)
    }

    @Provides
    fun provideCartRepository(
        cartApi: ProductCartApi,
        cartDao: CartDao,
        productDao: ProductDao,
        @ApplicationContext context: Context
    ): CartRepository {
        return CartRepository(cartApi, cartDao, productDao, context)
    }

    @Provides
    fun provideOrderRepository(
        orderApi: OrderApi,
        orderDao: OrderDao,
        productDao: ProductDao,
        @ApplicationContext context: Context
    ): OrderRepository {
        return OrderRepository(orderApi, orderDao, productDao, context)
    }

    @Provides
    fun provideExploreRepository(
        exploreDao: ExploreDao,
        searchHistoryDao: SearchHistoryDao
    ): ExploreRepository {
        return ExploreRepository(exploreDao, searchHistoryDao)
    }

}