package com.example.asm2f.data.di

import android.content.Context
import com.example.asm2f.data.repository.APIRepository
import com.example.asm2f.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideAPIRepository(): APIRepository = APIRepository()

    @Provides
    fun provideMovieRepository(@ApplicationContext context: Context): MovieRepository = MovieRepository(context)
}