package com.thirdwavelist.coficiando.core.data.db

import com.thirdwavelist.coficiando.core.data.db.CustomTypeConverters
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.Test
import java.util.UUID

class CustomTypeConvertersTest {

    private val converter = CustomTypeConverters()

    @Test
    fun `Given UUID, When converted to String, Then result matches toString()`() {
        // Given
        val uuid = UUID.randomUUID()

        // When
        val result = converter.fromUUID(uuid)

        // Then
        assertThat(result).isEqualTo(uuid.toString())
    }

    @Test
    fun `Given UUID compatible String, When converted to UUID, Then result matches UUID#parseString(String)`() {
        // Given
        val string = "0cdc16a0-5867-4e71-b1ee-24dbc572e1a2"

        // When
        val result = converter.toUUID(string)

        // Then
        assertThat(result).isEqualTo(UUID.fromString(string))
    }

    @Test
    fun `Given invalid String, When converted to UUID, Then exception is thrown`() {
        // Given
        val string = "-24dbc572e1a2="

        // When - Then
        assertThatIllegalArgumentException().isThrownBy {
            converter.toUUID(string)
        }.withMessage("Invalid UUID string: $string")
    }
}