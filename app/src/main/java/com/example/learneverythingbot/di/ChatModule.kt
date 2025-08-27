package com.example.learneverythingbot.di

import com.example.learneverythingbot.data.local.ChatLocalDataSource
import com.example.learneverythingbot.data.local.LocalDataSource
import com.example.learneverythingbot.data.remote.ChatRemoteDataSource
import com.example.learneverythingbot.data.remote.RemoteDataSource
import com.example.learneverythingbot.data.remote.retrofit.OpenAiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class ChatModule {
    @Provides
    fun providesOpenAiService(retrofit: Retrofit): OpenAiService{
        return retrofit.create(OpenAiService::class.java)
    }


}

@Module
@InstallIn(ViewModelComponent::class)
interface ChatModuleBinding{

    @Binds
    fun bindLocalDataSource(impl: ChatLocalDataSource): LocalDataSource

    @Binds
    fun bindRemoteDataSource(impl: ChatRemoteDataSource): RemoteDataSource
}