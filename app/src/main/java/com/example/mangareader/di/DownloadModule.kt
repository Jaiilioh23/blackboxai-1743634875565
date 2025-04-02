package com.example.mangareader.di

import android.content.Context
import com.example.mangareader.data.download.DownloadManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DownloadModule {

    @Provides
    @Singleton
    fun provideDownloadManager(
        @ApplicationContext context: Context
    ): DownloadManager {
        return DownloadManager(context)
    }
}