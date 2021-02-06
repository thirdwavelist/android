package com.thirdwavelist.coficiando.core.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thirdwavelist.coficiando.core.DbTest
import com.thirdwavelist.coficiando.core.domain.cafe.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CafeDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun givenAnEmptyDatabase_WhenInsertingOne_ThenEntityIsAdded() = runBlockingTest {
        // Given
        assertThat(db.cafeDao().getAll()).isEmpty()

        // When
        db.cafeDao().insert(TestEntity1)

        // Then
        assertThat(db.cafeDao().getAll()).satisfies {
            assertThat(it).hasSize(1)
            assertThat(it[0]).isEqualTo(TestEntity1)
        }
    }

    @Test
    fun givenAnEmptyDatabase_WhenInsertingMultiple_ThenEntitiesAreAdded() = runBlockingTest {
        // Given
        assertThat(db.cafeDao().getAll()).isEmpty()

        // When
        db.cafeDao().insertAll(listOf(TestEntity1, TestEntity2))

        // Then
        assertThat(db.cafeDao().getAll()).satisfies {
            assertThat(it).hasSize(2)
            assertThat(it).isEqualTo(listOf(TestEntity1, TestEntity2))
        }
    }

    @Test
    fun givenEntityInDb_WhenRetrievingGivenEntityById_ThenEntityIsRetrieved() = runBlockingTest {
        // Given
        db.cafeDao().insertAll(listOf(TestEntity1, TestEntity2))

        // When
        val retrievedEntity = db.cafeDao().get(TestEntity1.id)

        // Then
        assertThat(retrievedEntity).isEqualTo(TestEntity1)
    }

    @Suppress("BooleanLiteralArgument")
    private companion object {
        private val TestEntity1 = CafeItem(
                UUID.fromString("4029172e-c30e-40a9-957b-c85252db248a"),
                "Fekete",
                "https://assets.thirdwavelist.com/thumb/4029172e-c30e-40a9-957b-c85252db248a.jpg",
                SocialItem("", "", ""),
                "ChIJ-UU5E0PcQUcRP0iqvvmBTO8",
                GearInfoItem("La Marzocco", "Victoria Arduino Mythos, Mahlkönig EK43"),
                BeanInfoItem("", "Casino Mocca", true, false, true, false, false),
                BrewInfoItem(true, false, true, true, false, true)
        )
        private val TestEntity2 = CafeItem(
                UUID.fromString("8818c4e0-1e48-4107-9926-eac06ac5829c"),
                "Budapest Baristas",
                "https://assets.thirdwavelist.com/thumb/8818c4e0-1e48-4107-9926-eac06ac5829c.jpg",
                SocialItem("", "", ""),
                "ChIJ-UU5E0PcQUcRP0iqvvmBTO8",
                GearInfoItem("La Marzocco", "Eureka, Mazzer Robur"),
                BeanInfoItem("Ethiopia", "Coffee Collective", true, false, true, true, false),
                BrewInfoItem(true, false, true, true, false, false)
        )
    }
}