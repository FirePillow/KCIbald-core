package com.kcibald.services.core.handlers

import com.kcibald.services.core.QueryResults
import com.kcibald.services.core.proto.*
import com.kcibald.services.kcibald.URLKey
import io.vertx.core.Future
import io.vertx.core.MultiMap
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.Message
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.core.eventbus.requestAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.time.ZoneOffset

@ExtendWith(VertxExtension::class)
internal class DescribePostInterfaceTest {

    @Test
    fun region_notFound(vertx: Vertx) = runBlocking {
        val address = "test_address"

        val regionService = object : TestRegionService() {
            override suspend fun translateURLKeyToId(urlKey: URLKey): String? = null
        }
        val target = DescribePostInterface(vertx, address, regionService, TestPostService(), TestCommentService())
        target.start()

        val payload = DescribePostRequest(
            region = DescribePostRequest.Region.RUrlKey("not_exits"),
            post = DescribePostRequest.Post.PId("not_exits"),
            queryConfig = QueryConfig(amount = 5)
        )

        val responseMessage = vertx.eventBus().requestAwait<ByteArray>(
            address,
            payload.protoMarshal()
        )

        val response = DescribePostResponse.protoUnmarshal(responseMessage.body())

        val failure = (response.responseType as DescribePostResponse.ResponseType.Failure).failure
        assertEquals(DescribePostResponse.Failure.NOT_FOUND, failure)

        Unit
    }

    @Test
    fun post_notFound_by_id(vertx: Vertx) = runBlocking {
        val address = "test_address"

        val regionService = TestRegionService()
        val postService = object : TestPostService() {
            override suspend fun getPostFromId(regionId: String, postId: String): PostInfo? = null
        }

        val target = DescribePostInterface(vertx, address, regionService, postService, TestCommentService())
        target.start()

        val payload = DescribePostRequest(
            region = DescribePostRequest.Region.RId("exits"),
            post = DescribePostRequest.Post.PId("not_exits"),
            queryConfig = QueryConfig(amount = 5)
        )

        val responseMessage = vertx.eventBus().requestAwait<ByteArray>(
            address,
            payload.protoMarshal()
        )

        val response = DescribePostResponse.protoUnmarshal(responseMessage.body())

        val failure = (response.responseType as DescribePostResponse.ResponseType.Failure).failure
        assertEquals(DescribePostResponse.Failure.NOT_FOUND, failure)

        Unit
    }

    @Test
    fun post_notFound_by_url_key(vertx: Vertx) = runBlocking {
        val address = "test_address"

        val regionService = TestRegionService()
        val postService = object : TestPostService() {
            override suspend fun getPostFromUrlKey(regionId: String, postURLKey: URLKey): PostInfo? = null
        }

        val target = DescribePostInterface(vertx, address, regionService, postService, TestCommentService())
        target.start()

        val payload = DescribePostRequest(
            region = DescribePostRequest.Region.RId("exits"),
            post = DescribePostRequest.Post.PUrlKey("not_exits"),
            queryConfig = QueryConfig(amount = 5)
        )

        val responseMessage = vertx.eventBus().requestAwait<ByteArray>(
            address,
            payload.protoMarshal()
        )

        val response = DescribePostResponse.protoUnmarshal(responseMessage.body())

        val failure = (response.responseType as DescribePostResponse.ResponseType.Failure).failure
        assertEquals(DescribePostResponse.Failure.NOT_FOUND, failure)

        Unit
    }

    @Test
    fun corruption_comment_notFound(vertx: Vertx) = runBlocking {
        val address = "test_address"

        val regionService = TestRegionService()
        val postService = object : TestPostService() {
            override suspend fun getPostFromUrlKey(regionId: String, postURLKey: URLKey): PostInfo? = PostInfo()
        }
        val commentService = object : TestCommentService() {
            override suspend fun listCommentsUnderPost(
                regionId: String,
                postId: String,
                queryConfig: QueryConfig
            ): QueryResults<CommentInfo>? = null
        }

        val target = DescribePostInterface(vertx, address, regionService, postService, commentService)
        target.start()

        val payload = DescribePostRequest(
            region = DescribePostRequest.Region.RId("exits"),
            post = DescribePostRequest.Post.PUrlKey("exits"),
            queryConfig = QueryConfig(amount = 5)
        )

        val responseMessage = vertx.eventBus().requestAwait<ByteArray>(
            address,
            payload.protoMarshal()
        )

        val response = DescribePostResponse.protoUnmarshal(responseMessage.body())

        val failure = (response.responseType as DescribePostResponse.ResponseType.Failure).failure
        assertEquals(DescribePostResponse.Failure.ERROR, failure)

        Unit
    }

    @Test
    fun normal_query_by_rid_pid(vertx: Vertx) = runBlocking {
        val address = "address"
        val rId = "lalala"
        val pid = "pid"
        val testRegionService = object : TestRegionService() {
            override suspend fun translateURLKeyToId(urlKey: URLKey): String? {
                fail<Unit>("given region id")
//                unreachable
                return null
            }
        }

        val postInfo = PostInfo(
            pid,
            "url_key",
            "title",
            content = ""
        )

        val commentQueryResults = QueryResults(
            listOf(
                CommentInfo(
                    2,
                    "kk",
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    "content"
                )
            ),
            ""
        )

        val testPostService = object : TestPostService() {
            override suspend fun getPostFromId(regionId: String, postId: String): PostInfo? {
                assertEquals(pid, postId)
                return postInfo
            }
        }
        val testCommentService = object : TestCommentService() {
            override suspend fun listCommentsUnderPost(
                regionId: String,
                postId: String,
                queryConfig: QueryConfig
            ): QueryResults<CommentInfo>? {
                assertEquals(rId, regionId)
                assertEquals(pid, postId)
                return commentQueryResults
            }
        }
        val target = DescribePostInterface(vertx, address, testRegionService, testPostService, testCommentService)

        target.start()

        val request = DescribePostRequest(
            region = DescribePostRequest.Region.RId(rId),
            post = DescribePostRequest.Post.PId(pid),
            queryConfig = QueryConfig(
                amount = 5
            )
        )

        val responseMessage = vertx.eventBus().requestAwait<ByteArray>(
            address,
            request.protoMarshal()
        )

        val responseType = DescribePostResponse.protoUnmarshal(responseMessage.body()).responseType
        if (responseType is DescribePostResponse.ResponseType.Failure) {
            println(responseType)
            fail<Unit>()

            return@runBlocking
        }

        val response = (responseType as DescribePostResponse.ResponseType.Success).success

        assertEquals(postInfo, response.postInfo)
        assertEquals(commentQueryResults.result, response.comment)
        assertEquals(commentQueryResults.queryMark, response.queryMark)

        Unit
    }

    @Test
    fun normal_query_by_rkey_pkey(vertx: Vertx) = runBlocking {
        val address = "address"
        val rkey = "region"
        val pkey = "post"

        val rid = "region_id"
        var ridTrapped = false
        val testRegionService = object : TestRegionService() {
            override suspend fun translateURLKeyToId(urlKey: URLKey): String? {
                ridTrapped = true
                return rid
            }
        }

        val postInfo = PostInfo(
            id = "id",
            urlKey = pkey,
            title = "title",
            author = "author_key",
            creationTimeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            content = "content"
        )

        val testPostService = object : TestPostService() {
            override suspend fun getPostFromUrlKey(regionId: String, postURLKey: URLKey): PostInfo? {
                assertEquals(rid, regionId)
                assertEquals(pkey, postURLKey)
                return postInfo
            }
        }

        val commentQueryResults = QueryResults<CommentInfo>(
            listOf(
                CommentInfo(
                    1,
                    "author",
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    "content"
                )
            ),
            "blahblah"
        )

        val commentService = object : TestCommentService() {
            override suspend fun listCommentsUnderPost(
                regionId: String,
                postId: String,
                queryConfig: QueryConfig
            ): QueryResults<CommentInfo>? {
                return commentQueryResults
            }
        }

        val target = DescribePostInterface(vertx, address, testRegionService, testPostService, commentService)

        target.start()

        val request = DescribePostRequest(
            region = DescribePostRequest.Region.RUrlKey(rkey),
            post = DescribePostRequest.Post.PUrlKey(pkey),
            queryConfig = QueryConfig(
                "",
                amount = 5
            )
        )

        val responseMessage = vertx.eventBus().requestAwait<ByteArray>(
            address,
            request.protoMarshal()
        )

        assertTrue(ridTrapped)

        val response = DescribePostResponse.protoUnmarshal(responseMessage.body()).responseType

        if (response is DescribePostResponse.ResponseType.Failure) {
            fail<String>(response.toString())
        }

        val success = (response as DescribePostResponse.ResponseType.Success).success

        assertEquals(commentQueryResults.result, success.comment)
        assertEquals(commentQueryResults.queryMark, success.queryMark)
        assertEquals(postInfo, success.postInfo)

        Unit
    }

    @Test
    fun errorResultSupplier(vertx: Vertx) = runBlocking {
        @Suppress("UNCHECKED_CAST")
        assertEquals(
            DescribePostInterface.errorResult,
            DescribePostInterface.errorResultSupplier(
                DescribePostInterface(
                    vertx,
                    "balah",
                    TestRegionService(),
                    TestPostService(),
                    TestCommentService()
                ),
                object : Message<ByteArray> {
                    override fun replyAddress(): String = fail()

                    override fun isSend(): Boolean = fail()

                    override fun body(): ByteArray = ByteArray(0)

                    override fun address(): String = fail()

                    override fun reply(message: Any?, options: DeliveryOptions?) {}

                    override fun headers(): MultiMap = fail()

                    override fun <R : Any?> replyAndRequest(
                        message: Any?,
                        options: DeliveryOptions?
                    ): Future<Message<R>> = fail()
                },
                Throwable()
            )
        )
    }
}