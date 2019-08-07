package com.kcibald.services.core.handlers

import com.kcibald.interfaces.EventResult
import com.kcibald.interfaces.ProtobufEventResult
import com.kcibald.interfaces.ServiceInterface
import com.kcibald.services.core.proto.DescribeRegionRequest
import com.kcibald.services.core.proto.DescribeRegionResponse
import com.kcibald.services.core.proto.Failure
import com.kcibald.services.core.services.PostService
import com.kcibald.services.core.services.RegionService
import com.kcibald.utils.w
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class DescribeRegionInterface(
    vertx: Vertx,
    eventbusAddress: String,
    private val regionService: RegionService,
    private val postService: PostService
) : ServiceInterface<ByteArray>(vertx, eventbusAddress, errorEventResult) {

    override suspend fun handle(message: Message<ByteArray>): EventResult {
        val request = DescribeRegionRequest.protoUnmarshal(message.body())

        val regionInfo = when (val medium = request.key!!) {
            is DescribeRegionRequest.Key.Id ->
                regionService.getRegionById(medium.id)
            is DescribeRegionRequest.Key.UrlKey ->
                regionService.getRegionByUrlKey(medium.urlKey)
        } ?: return notFoundEventResult

        val config = request.config!!

//        short cut
        if (config.amount == 0) {
            val response = DescribeRegionResponse(
                responseType = DescribeRegionResponse.ResponseType.Success(
                    DescribeRegionResponse.Success(
                        regionInfo = regionInfo
                    )
                )
            )
            return ProtobufEventResult(response)
        }

        val postHeads = postService.getPostHeadsUnderRegion(regionInfo.id, config)

        if (postHeads == null) {
            logger.w { "Possible data structure corruption! region $regionInfo can not find associated post heads" }
            return errorEventResult
        }

        return ProtobufEventResult(
            DescribeRegionResponse(
                DescribeRegionResponse.ResponseType.Success(
                    DescribeRegionResponse.Success(
                        regionInfo = regionInfo,
                        topPosts = postHeads.result,
                        queryMark = postHeads.queryMark
                    )
                )
            )
        )
    }

    override suspend fun starting() {
        regionService.start()
        postService.start()
    }

    override suspend fun stopping() {
        regionService.stop()
        postService.stop()
    }

    private val notFoundEventResult = ProtobufEventResult(
        DescribeRegionResponse(
            DescribeRegionResponse.ResponseType.Failure(Failure.NOT_FOUND)
        )
    )

    companion object {
        private val errorEventResult = ProtobufEventResult(
            DescribeRegionResponse(
                DescribeRegionResponse.ResponseType.Failure(Failure.ERROR)
            )
        )
    }

}