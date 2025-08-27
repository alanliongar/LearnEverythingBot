package com.example.learneverythingbot.di

import android.app.Application
import androidx.room.Room
import com.example.learneverythingbot.data.local.dao.ChatHistoryDao
import com.example.learneverythingbot.data.local.database.AppDatabase
import com.example.learneverythingbot.data.remote.RetrofitClient
import com.example.learneverythingbot.di.DispatcherIO
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
    fun provideCineNowDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "database-learneverythingbot"
        ).build()
    }

    @Provides
    fun provideChatHistoryDao(roomDatabase: AppDatabase): ChatHistoryDao {
        return roomDatabase.chatHistoryDao()
    }

    @Provides
    fun provideRetrofit(): Retrofit {
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