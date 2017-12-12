package com.thirdwavelist.coficiando.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.thirdwavelist.coficiando.di.qualifiers.AppContext
import com.thirdwavelist.coficiando.storage.db.Database
import com.thirdwavelist.coficiando.storage.db.cafe.CafeDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StorageModule {
    private const val DB_NAME = "app-database"

    @Provides
    @Singleton
    @JvmStatic
    fun provideDatabase(@AppContext context: Context) =
        Room.databaseBuilder(context, Database::class.java, DB_NAME).build()

    @Provides
    @Singleton
    @JvmStatic
    fun provideCafeDao(database: Database): CafeDao = database.cafeDao()
}