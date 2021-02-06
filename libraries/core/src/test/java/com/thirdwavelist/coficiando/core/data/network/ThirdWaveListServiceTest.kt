package com.thirdwavelist.coficiando.core.data.network

import com.google.gson.Gson
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.thirdwavelist.coficiando.core.data.FileReaderUtil
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.UUID

class ThirdWaveListServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: ThirdWaveListService

    @Before
    fun before() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(OkHttpClient().newBuilder().build())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .build().create(ThirdWaveListService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Given successful getCafes response when getCafes is called then expected data object returned`(): Unit = runBlocking {
        // Given
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(FileReaderUtil("getAll_success.json").content)
        mockWebServer.enqueue(response)

        // When
        val result = api.getCafes()

        // Then
        assertThat((result as NetworkResponse.Success)).satisfies {
            assertThat(it.code).isEqualTo(200)
            assertThat(it.body.size).isEqualTo(2)
            assertThat(it.body).usingRecursiveComparison().isEqualTo(CafeItemResponseBuilder.multiple())
        }
    }

    @Test
    fun `Given failed getCafes response when getCafes is called then expected data object returned`(): Unit = runBlocking {
        // Given
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
                .setBody(FileReaderUtil("getAll_error.json").content)
        mockWebServer.enqueue(response)

        // When
        val result = api.getCafes()

        // Then
        assertThat((result as NetworkResponse.ServerError)).satisfies {
            assertThat(it.code).isEqualTo(403)
            assertThat(it.body).usingRecursiveComparison().isEqualTo(CafeItemResponseBuilder.forbidden())
        }
    }

    @Test
    fun `Given cafe id and successful getCafe response when getCafes is called then expected data object returned`(): Unit = runBlocking {
        // Given
        val cafeId = UUID.fromString("5a5b4020-315e-4504-9068-a6381dd45383")
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(FileReaderUtil("getOne_success.json").content)
        mockWebServer.enqueue(response)

        // When
        val result = api.getCafe(cafeId)

        // Then
        assertThat((result as NetworkResponse.Success)).satisfies {
            assertThat(it.code).isEqualTo(200)
            assertThat(it.body).usingRecursiveComparison().isEqualTo(CafeItemResponseBuilder.single())
        }
    }

    @Test
    fun `Given forbidden getCafe response when getCafes is called then expected data object returned`(): Unit = runBlocking {
        // Given
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
                .setBody(FileReaderUtil("getOne_error.json").content)
        mockWebServer.enqueue(response)

        // When
        val result = api.getCafe(UUID.randomUUID())

        // Then
        assertThat((result as NetworkResponse.ServerError)).satisfies {
            assertThat(it.code).isEqualTo(403)
            assertThat(it.body).usingRecursiveComparison().isEqualTo(CafeItemResponseBuilder.forbidden())
        }
    }
}