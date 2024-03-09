package com.javokhirbekcoder.uzbekmusic.di

import com.javokhirbekcoder.uzbekmusic.repository.api.ApiClient
import com.javokhirbekcoder.uzbekmusic.repository.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
Created by Javokhirbek on 04/03/2024 at 23:34
*/


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAoiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

/*    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java,
            "app_database").build()
    }*/

  /*  @Singleton
    @Provides
    fun providePostDao(appDatabase: AppDatabase) : PostDao{
        return appDatabase.postDao()
    }
*/
  /*  @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context) : NetworkHelper{
        return NetworkHelper(context)
    }
*/
}