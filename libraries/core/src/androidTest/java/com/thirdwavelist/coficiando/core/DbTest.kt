package com.thirdwavelist.coficiando.core

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.thirdwavelist.coficiando.core.data.db.Database
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

abstract class DbTest {

    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    private lateinit var _db: Database

    val db: Database
        get() = _db

    @Before
    fun initDb() {
        _db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            Database::class.java
        ).build()
    }

    @After
    fun closeDb() {
        countingTaskExecutorRule.drainTasks(10, TimeUnit.SECONDS)
        _db.close()
    }
}