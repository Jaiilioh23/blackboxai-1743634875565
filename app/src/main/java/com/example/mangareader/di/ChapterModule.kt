package com.example.mangareader.di

import com.example.mangareader.data.repository.ChapterRepository
import com.example.mangareader.data.repository.ChapterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChapterModule {

    @Binds
    @Singleton
    abstract fun bindChapterRepository(
        impl: ChapterRepositoryImpl
    ): ChapterRepository
}