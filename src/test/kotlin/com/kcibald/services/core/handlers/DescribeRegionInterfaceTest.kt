package com.kcibald.services.core.handlers

import com.kcibald.services.core.QueryResults
import com.kcibald.services.core.proto.*
import com.kcibald.services.core.services.PostService
import com.kcibald.services.core.services.RegionService
import com.kcibald.services.kcibald.URLKey
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.core.eventbus.requestAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.time.ZoneOffset

@ExtendWith(VertxExtension::class)
internal class DescribeRegionInterfaceTest {

    open class TestRegionService : RegionService {
        override suspend fun getRegionById(id: String): RegionInfo? {
            throw AssertionError()
        }

        override suspend fun getRegionByUrlKey(urlKey: URLKey): RegionInfo? {
            throw AssertionError()
        }

        override suspend fun translateURLKeyToId(): String? = ""

        override suspend fun start() {}

        override suspend fun stop() {}
    }

    open class TestPostService : PostService {
        override suspend fun getPostHeadsUnderRegion(
            regionId: String,
            queryConfig: QueryConfig
        ): QueryResults<PostHead>? {
            throw AssertionError()
        }

        override suspend fun getPostsUnderRegion(regionId: String, queryConfig: QueryConfig): QueryResults<PostInfo>? {
            throw AssertionError()
        }

        override suspend fun start() {}

        override suspend fun stop() {}
    }

    @Test
    fun query_id_no_posts(vertx: Vertx) = runBlocking {
        val expectedId = "akjdljladjf"
        val regionInfo = RegionInfo(
            id = expectedId,
            urlKey = "lol",
            name = "lol",
            description = "lol_lol_lol",
            avatar = "avatar"
        )
        val testRegionService = object : TestRegionService() {
            override suspend fun getRegionById(id: String): RegionInfo {
                assertEquals(expectedId, id)
                return regionInfo
            }
        }
        val eventBusAddr = "events"

        val target = DescribeRegionInterface(vertx, eventBusAddr, testRegionService, TestPostService())

        target.start()
        val response = vertx.eventBus().requestAwait<ByteArray>(
            eventBusAddr, DescribeRegionRequest(
                DescribeRegionRequest.Key.Id(expectedId),
                QueryConfig()
            ).protoMarshal()
        ).body().let { DescribeRegionResponse.protoUnmarshal(it) }.responseType

        response!!

        response as DescribeRegionResponse.ResponseType.Success

        assertEquals(regionInfo, response.success.regionInfo)

        target.stop()

        Unit
    }

    @Test
    fun query_url_key_no_posts(vertx: Vertx) = runBlocking {
        val expectedUrlKey = "akjdljladjf"
        val regionInfo = RegionInfo(
            id = "kjakjdlajf",
            urlKey = expectedUrlKey,
            name = "lol",
            description = "lol_lol_lol",
            avatar = "avatar"
        )
        val testRegionService = object : TestRegionService() {
            override suspend fun getRegionByUrlKey(urlKey: URLKey): RegionInfo {
                assertEquals(expectedUrlKey, urlKey)
                return regionInfo
            }
        }
        val eventBusAddr = "events"

        val target = DescribeRegionInterface(vertx, eventBusAddr, testRegionService, TestPostService())

        target.start()
        val response = vertx.eventBus().requestAwait<ByteArray>(
            eventBusAddr, DescribeRegionRequest(
                DescribeRegionRequest.Key.UrlKey(expectedUrlKey),
                QueryConfig()
            ).protoMarshal()
        ).body().let { DescribeRegionResponse.protoUnmarshal(it) }.responseType

        response!!

        response as DescribeRegionResponse.ResponseType.Success

        assertEquals(regionInfo, response.success.regionInfo)

        target.stop()

        Unit
    }


    @Test
    fun data_corruption(vertx: Vertx) = runBlocking {
        val regionInfo = RegionInfo(
            id = "akjdljladjf",
            urlKey = "lol",
            name = "lol",
            description = "lol_lol_lol",
            avatar = "avatar"
        )
        val testRegionService = object : TestRegionService() {
            override suspend fun getRegionById(id: String) = regionInfo
        }

        val testPostService = object : TestPostService() {
            override suspend fun getPostHeadsUnderRegion(
                regionId: String,
                queryConfig: QueryConfig
            ): QueryResults<PostHead>? = null
        }

        val eventBusAddr = "events"

        val target = DescribeRegionInterface(vertx, eventBusAddr, testRegionService, testPostService)

        target.start()

        val response = vertx.eventBus().requestAwait<ByteArray>(
            eventBusAddr, DescribeRegionRequest(
                DescribeRegionRequest.Key.Id("akjdljladjf"),
                QueryConfig(amount = 1)
            ).protoMarshal()
        ).body().let(DescribeRegionResponse.Companion::protoUnmarshal).responseType

        response!!

        response as DescribeRegionResponse.ResponseType.Failure

        assertEquals(Failure.ERROR, response.failure)

        target.stop()

        Unit

    }


    @Test
    fun normal(vertx: Vertx) = runBlocking {
        val regionInfo = RegionInfo(
            id = "akjdljladjf",
            urlKey = "lol",
            name = "lol",
            description = "lol_lol_lol",
            avatar = "avatar"
        )
        val testRegionService = object : TestRegionService() {
            override suspend fun getRegionById(id: String) = regionInfo
        }

        val posts = listOf(
            PostHead(
                urlKey = "url",
                title = "url",
                author = "klakljdkjf",
                creationTimeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )

        val mark = "KJDSKLJDJKLJFKF"

        val testPostService = object : TestPostService() {
            override suspend fun getPostHeadsUnderRegion(
                regionId: String,
                queryConfig: QueryConfig
            ): QueryResults<PostHead>? = QueryResults(
                posts,
                mark
            )
        }

        val eventBusAddr = "events"

        val target = DescribeRegionInterface(vertx, eventBusAddr, testRegionService, testPostService)

        target.start()

        val response = vertx.eventBus().requestAwait<ByteArray>(
            eventBusAddr, DescribeRegionRequest(
                DescribeRegionRequest.Key.Id("akjdljladjf"),
                QueryConfig(amount = 1)
            ).protoMarshal()
        )
            .body()
            .let(DescribeRegionResponse.Companion::protoUnmarshal)
            .responseType

        response!!

        response as DescribeRegionResponse.ResponseType.Success

        val (resultRegion, topPosts, reMark) = response.success
        assertEquals(regionInfo, resultRegion)
        assertEquals(posts, topPosts)
        assertEquals(mark, reMark)

        target.stop()

        Unit
    }

    @Test
    fun notFound(vertx: Vertx) = runBlocking {
        val testRegionService = object : TestRegionService() {
            override suspend fun getRegionById(id: String) = null
        }

        val eventBusAddr = "events"

        val target = DescribeRegionInterface(vertx, eventBusAddr, testRegionService, TestPostService())

        target.start()

        val response = vertx.eventBus().requestAwait<ByteArray>(
            eventBusAddr, DescribeRegionRequest(
                DescribeRegionRequest.Key.Id("akjdljladjf"),
                QueryConfig(amount = 1)
            ).protoMarshal()
        )
            .body()
            .let(DescribeRegionResponse.Companion::protoUnmarshal)
            .responseType

        response!!

        response as DescribeRegionResponse.ResponseType.Failure

        assertEquals(Failure.NOT_FOUND, response.failure)

        target.stop()

        Unit
    }

    @Test
    fun exception_region_service(vertx: Vertx) = runBlocking {
        val testRegionService = object : TestRegionService() {
            override suspend fun getRegionById(id: String): RegionInfo {
                throw RuntimeException(":(")
            }
        }
        val target = DescribeRegionInterface(vertx, "default", testRegionService, TestPostService())
        target.start()
        val response = vertx.eventBus().requestAwait<ByteArray>(
            "default", DescribeRegionRequest(
                DescribeRegionRequest.Key.Id("kjalkjdljaf"),
                QueryConfig()
            ).protoMarshal()
        ).body().let { DescribeRegionResponse.protoUnmarshal(it) }.responseType

        response!!

        response as DescribeRegionResponse.ResponseType.Failure

        assertEquals(Failure.ERROR, response.failure)

        target.stop()
    }

    @Test
    fun exception_post_service(vertx: Vertx) = runBlocking {
        val regionInfo = RegionInfo(
            id = "akjdljladjf",
            urlKey = "lol",
            name = "lol",
            description = "lol_lol_lol",
            avatar = "avatar"
        )

        val testRegionService = object : TestRegionService() {
            override suspend fun getRegionById(id: String) = regionInfo
        }

        val testPostService = object : TestPostService() {
            override suspend fun getPostHeadsUnderRegion(
                regionId: String,
                queryConfig: QueryConfig
            ): QueryResults<PostHead>? = throw RuntimeException(":(")
        }

        val eventBusAddr = "events"

        val target = DescribeRegionInterface(vertx, eventBusAddr, testRegionService, testPostService)

        target.start()

        val response = vertx.eventBus().requestAwait<ByteArray>(
            eventBusAddr, DescribeRegionRequest(
                DescribeRegionRequest.Key.Id("akjdljladjf"),
                QueryConfig(amount = 1)
            ).protoMarshal()
        )
            .body()
            .let(DescribeRegionResponse.Companion::protoUnmarshal)
            .responseType

        response!!

        response as DescribeRegionResponse.ResponseType.Failure

        assertEquals(Failure.ERROR, response.failure)

        target.stop()
    }

    @Test
    fun start(vertx: Vertx) {
        var isRegionServiceStarted = false
        val testRegionService = object : TestRegionService() {
            override suspend fun start() {
                isRegionServiceStarted = true
            }
        }
        var isPostServiceStarted = false
        val testPostService = object : TestPostService() {
            override suspend fun start() {
                isPostServiceStarted = true
            }
        }
        val target = DescribeRegionInterface(vertx, "default", testRegionService, testPostService)
        runBlocking {
            target.start()
        }
        data class State(
            val isRegionServiceStarted: Boolean,
            val isPostServiceStarted: Boolean
        )
        assertEquals(State(true, true), State(isRegionServiceStarted, isPostServiceStarted))
    }

    @Test
    fun stop(vertx: Vertx) {
        var isRegionServiceStopped = false
        val testRegionService = object : TestRegionService() {
            override suspend fun stop() {
                isRegionServiceStopped = true
            }
        }
        var isPostServiceStopped = false
        val testPostService = object : TestPostService() {
            override suspend fun stop() {
                isPostServiceStopped = true
            }
        }
        val target = DescribeRegionInterface(vertx, "default", testRegionService, testPostService)
        runBlocking {
            target.start()
            target.stop()
        }
        data class State(
            val isRegionServiceStopped: Boolean,
            val isPostServiceStopped: Boolean
        )
        assertEquals(State(true, true), State(isRegionServiceStopped, isPostServiceStopped))
    }
}