package com.thirdwavelist.coficiando.core.di.modules

import android.content.Context
import androidx.room.Room
import com.thirdwavelist.coficiando.core.data.db.Database
import com.thirdwavelist.coficiando.core.data.db.cafe.CafeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
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