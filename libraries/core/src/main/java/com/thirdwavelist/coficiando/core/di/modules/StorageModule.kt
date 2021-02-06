package com.thirdwavelist.coficiando.core.di.modules

import android.content.Context
import androidx.room.Room
import com.thirdwavelist.coficiando.core.data.db.CafeDao
import com.thirdwavelist.coficiando.core.data.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    private const val DB_NAME = "app-database.db"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
            Room.databaseBuilder(context, Database::class.java, DB_NAME).build()

    @Provides
    @Singleton
    fun provideCafeDao(database: Database): CafeDao = database.cafeDao()
}