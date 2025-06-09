package ru.android.nectar.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.android.nectar.data.api.AuthApi
import ru.android.nectar.data.api.OrderApi
import ru.android.nectar.data.api.ProductApiService
import ru.android.nectar.data.api.ProductCartApi
import ru.android.nectar.data.api.ProductFavoriteApi
import ru.android.nectar.data.repository.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.103:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    fun provideProductFavoriteApi(retrofit: Retrofit): ProductFavoriteApi {
        return retrofit.create(ProductFavoriteApi::class.java)
    }

    @Provides
    fun provideProductCartApi(retrofit: Retrofit): ProductCartApi {
        return retrofit.create(ProductCartApi::class.java)
    }

    @Provides
    fun provideOrderApi(retrofit: Retrofit): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }


}
