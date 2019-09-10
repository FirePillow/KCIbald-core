package com.kcibald.services.core.handlers

import com.kcibald.interfaces.EventResult
import com.kcibald.interfaces.ProtobufEventResult
import com.kcibald.interfaces.ServiceInterface
import com.kcibald.services.core.proto.DescribePostRequest
import com.kcibald.services.core.proto.DescribePostResponse
import com.kcibald.services.core.proto.DescribePostResponse.ResponseType
import com.kcibald.services.core.services.CommentService
import com.kcibald.services.core.services.PostService
import com.kcibald.services.core.services.RegionService
import com.kcibald.utils.w
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class DescribePostInterface(
    vertx: Vertx,
    eventbusAddress: String,
    private val regionService: RegionService,
    private val postService: PostService,
    private val commentService: CommentService
) : ServiceInterface<ByteArray>(vertx, eventbusAddress, errorResultSupplier) {

    override suspend fun handle(message: Message<ByteArray>): EventResult {
        val describePostRequest = DescribePostRequest.protoUnmarshal(message.body())
        val regionId = when (val medium = describePostRequest.region!!) {
            is DescribePostRequest.Region.RId ->
                medium.rId
            is DescribePostRequest.Region.RUrlKey ->
                regionService.translateURLKeyToId(medium.rUrlKey)
        } ?: return notFoundResult

        val post = when (val medium = describePostRequest.post!!) {
            is DescribePostRequest.Post.PId ->
                postService.getPostFromId(regionId, medium.pId)
            is DescribePostRequest.Post.PUrlKey ->
                postService.getPostFromUrlKey(regionId, medium.pUrlKey)
        } ?: return notFoundResult

        val comments = commentService.listCommentsUnderPost(
            regionId,
            post.id,
            describePostRequest.queryConfig!!
        )

        if (comments == null) {
            logger.w { "Possible data structure corruption! post $post can not find associated comments" }
            return errorResult
        }

        val response = DescribePostResponse(
            ResponseType.Success(
                DescribePostResponse.Success(
                    post,
                    comments.queryMark,
                    comments.result
                )
            )
        )

        return ProtobufEventResult(response)
    }

    private val notFoundResult = ProtobufEventResult(
        DescribePostResponse(
            ResponseType.Failure(DescribePostResponse.Failure.NOT_FOUND)
        )
    )

    companion object {

        internal val errorResultSupplier:
                suspend ServiceInterface<ByteArray>.(Message<ByteArray>, Throwable) -> EventResult? =
            { _, _ ->
                errorResult
            }

        internal val errorResult = ProtobufEventResult(
            DescribePostResponse(
                ResponseType.Failure(DescribePostResponse.Failure.ERROR)
            )
        )

    }
}