package com.thirdwavelist.coficiando.storage.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.Room
import android.arch.persistence.room.testing.MigrationTestHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.thirdwavelist.coficiando.storage.db.cafe.BeanInfoItem
import com.thirdwavelist.coficiando.storage.db.cafe.BrewInfoItem
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.db.cafe.GearInfoItem
import com.thirdwavelist.coficiando.storage.db.cafe.SocialItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {

    companion object {
        private const val DB_NAME = "migration-test"
    }

    @Rule
    @JvmField
    val roomHelper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        Database::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun givenDatabaseInVersionOneWhenMigrationToVersionTwoThenDataIsCorrect() {
        // Given
        val db = givenDatabaseWithVersion(1)
        val city = givenCorrectCafe()
        db.insertCafe(city)
        db.close()

        // When
        roomHelper.runMigrationsAndValidate(DB_NAME, 2, true, Database.Migrations.FROM_1_TO_2)
        val result = getMigratedRoomDatabase().cafeDao().getAll().blockingFirst()

        // Then
        assert(result.first().id == city.id)
        assert(result.first().city.isNullOrBlank())
    }

    private fun givenDatabaseWithVersion(version: Int): SupportSQLiteDatabase {
        return roomHelper.createDatabase(DB_NAME, version).apply { roomHelper.closeWhenFinished(this@apply) }
    }

    private fun givenCorrectCafe(): CafeItem {
        return CafeItem(
            id = UUID.fromString("4e0cea4f-5b69-4a1d-9aec-d3323fbbfac6"),
            name = "OneCup",
            thumbnail = Uri.parse("https://assets.thirdwavelist.com/thumb/4e0cea4f-5b69-4a1d-9aec-d3323fbbfac6.jpg"),
            city = "Budapest",
            social = SocialItem(Uri.EMPTY, Uri.EMPTY, Uri.EMPTY),
            beanInfo = BeanInfoItem("", "", true, false, true, false, true),
            googlePlaceId = "ChIJ3VrJAVvcQUcRUi3b2SKkhPo",
            gearInfo = GearInfoItem("", ""),
            brewInfo = BrewInfoItem(true, false, true, false, true, false)
        )
    }

    private fun getMigratedRoomDatabase(): Database {
        return Room.databaseBuilder(InstrumentationRegistry.getTargetContext(), Database::class.java, DB_NAME)
            .addMigrations(Database.Migrations.FROM_1_TO_2)
            .build()
            .apply { roomHelper.closeWhenFinished(this@apply) }
    }

    private fun SupportSQLiteDatabase.insertCafe(cafe: CafeItem) {
        this.insert("cafes", SQLiteDatabase.CONFLICT_REPLACE, cafe.toContentValues() )
    }

    private fun CafeItem.toContentValues(): ContentValues {
        return ContentValues().apply {
            this.put("id", this@toContentValues.id.toString())
            this.put("name", this@toContentValues.name)
            this.put("thumb", this@toContentValues.thumbnail.toString())
            this.put("place_id", this@toContentValues.googlePlaceId)
            this.put("facebookUri", this@toContentValues.social.facebookUri.toString())
            this.put("instagramUri", this@toContentValues.social.instagramUri.toString())
            this.put("homepageUri", this@toContentValues.social.homepageUri.toString())
            this.put("espressoMachineName", this@toContentValues.gearInfo.espressoMachineName)
            this.put("grinderMachineName", this@toContentValues.gearInfo.grinderMachineName)
            this.put("origin", this@toContentValues.beanInfo.origin)
            this.put("roaster", this@toContentValues.beanInfo.roaster)
            this.put("hasSingleOrigin", this@toContentValues.beanInfo.hasSingleOrigin)
            this.put("hasBlendOrigin", this@toContentValues.beanInfo.hasBlendOrigin)
            this.put("hasLightRoast", this@toContentValues.beanInfo.hasLightRoast)
            this.put("hasMediumRoast", this@toContentValues.beanInfo.hasMediumRoast)
            this.put("hasDarkRoast", this@toContentValues.beanInfo.hasDarkRoast)
            this.put("hasEspresso", this@toContentValues.brewInfo.hasEspresso)
            this.put("hasAeropress", this@toContentValues.brewInfo.hasAeropress)
            this.put("hasPourOver", this@toContentValues.brewInfo.hasPourOver)
            this.put("hasColdBrew", this@toContentValues.brewInfo.hasColdBrew)
            this.put("hasSyphon", this@toContentValues.brewInfo.hasSyphon)
            this.put("hasFullImmersive", this@toContentValues.brewInfo.hasFullImmersive)
        }
    }
}