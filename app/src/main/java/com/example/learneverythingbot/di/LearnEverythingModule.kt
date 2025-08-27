package com.example.learneverythingbot.di

import android.app.Application
import androidx.room.Room
import com.example.learneverythingbot.data.local.room.ChatHistoryDao
import com.example.learneverythingbot.data.local.room.AppDatabase
import com.example.learneverythingbot.data.remote.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class LearnEverythingModule {
    @Provides
    fun procidesAppDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "database-learneverythingbot"
        ).build()
    }

    @Provides
    fun providesChatHistoryDao(roomDatabase: AppDatabase): ChatHistoryDao {
        return roomDatabase.chatHistoryDao()
    }

    @Provides
    fun providesRetrofit(): Retrofit {
        return RetrofitClient.retrofitInstance
    }

    @Provides
    @DispatcherIO
    fun providesDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun providesLongType(): Long {
        return 0L
    }

}