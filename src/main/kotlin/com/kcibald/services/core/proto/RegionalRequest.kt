package com.kcibald.services.core.proto

data class DescribeRegionRequest(
    val key: Key? = null,
    val config: com.kcibald.services.core.proto.QueryConfig? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<DescribeRegionRequest> {
    sealed class Key {
        data class Id(val id: String = "") : Key()
        data class UrlKey(val urlKey: String = "") : Key()
    }

    override operator fun plus(other: DescribeRegionRequest?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<DescribeRegionRequest> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = DescribeRegionRequest.protoUnmarshalImpl(u)
    }
}

data class DescribeRegionResponse(
    val responseType: ResponseType? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<DescribeRegionResponse> {
    sealed class ResponseType {
        data class Success(val success: com.kcibald.services.core.proto.DescribeRegionResponse.Success) : ResponseType()
        data class Failure(
            val failure: com.kcibald.services.core.proto.Failure = com.kcibald.services.core.proto.Failure.fromValue(
                0
            )
        ) : ResponseType()
    }

    override operator fun plus(other: DescribeRegionResponse?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<DescribeRegionResponse> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = DescribeRegionResponse.protoUnmarshalImpl(u)
    }

    data class Success(
        val regionInfo: com.kcibald.services.core.proto.RegionInfo? = null,
        val topPosts: List<com.kcibald.services.core.proto.PostHead> = emptyList(),
        val queryMark: String = "",
        val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message<Success> {
        override operator fun plus(other: Success?) = protoMergeImpl(other)
        override val protoSize by lazy { protoSizeImpl() }
        override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
        companion object : pbandk.Message.Companion<Success> {
            override fun protoUnmarshal(u: pbandk.Unmarshaller) = Success.protoUnmarshalImpl(u)
        }
    }
}

private fun DescribeRegionRequest.protoMergeImpl(plus: DescribeRegionRequest?): DescribeRegionRequest = plus?.copy(
    key = plus.key ?: key,
    config = config?.plus(plus.config) ?: plus.config,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun DescribeRegionRequest.protoSizeImpl(): Int {
    var protoSize = 0
    when (key) {
        is DescribeRegionRequest.Key.Id -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(key.id)
        is DescribeRegionRequest.Key.UrlKey -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(key.urlKey)
    }
    if (config != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(config)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DescribeRegionRequest.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (key is DescribeRegionRequest.Key.Id) protoMarshal.writeTag(10).writeString(key.id)
    if (key is DescribeRegionRequest.Key.UrlKey) protoMarshal.writeTag(18).writeString(key.urlKey)
    if (config != null) protoMarshal.writeTag(26).writeMessage(config)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DescribeRegionRequest.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DescribeRegionRequest {
    var key: DescribeRegionRequest.Key? = null
    var config: com.kcibald.services.core.proto.QueryConfig? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DescribeRegionRequest(key, config, protoUnmarshal.unknownFields())
        10 -> key = DescribeRegionRequest.Key.Id(protoUnmarshal.readString())
        18 -> key = DescribeRegionRequest.Key.UrlKey(protoUnmarshal.readString())
        26 -> config = protoUnmarshal.readMessage(com.kcibald.services.core.proto.QueryConfig.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun DescribeRegionResponse.protoMergeImpl(plus: DescribeRegionResponse?): DescribeRegionResponse = plus?.copy(
    responseType = when {
        responseType is DescribeRegionResponse.ResponseType.Success && plus.responseType is DescribeRegionResponse.ResponseType.Success ->
            DescribeRegionResponse.ResponseType.Success(responseType.success + plus.responseType.success)
        else ->
            plus.responseType ?: responseType
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun DescribeRegionResponse.protoSizeImpl(): Int {
    var protoSize = 0
    when (responseType) {
        is DescribeRegionResponse.ResponseType.Success -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(responseType.success)
        is DescribeRegionResponse.ResponseType.Failure -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(responseType.failure)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DescribeRegionResponse.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (responseType is DescribeRegionResponse.ResponseType.Success) protoMarshal.writeTag(10).writeMessage(responseType.success)
    if (responseType is DescribeRegionResponse.ResponseType.Failure) protoMarshal.writeTag(16).writeEnum(responseType.failure)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DescribeRegionResponse.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DescribeRegionResponse {
    var responseType: DescribeRegionResponse.ResponseType? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DescribeRegionResponse(responseType, protoUnmarshal.unknownFields())
        10 -> responseType = DescribeRegionResponse.ResponseType.Success(protoUnmarshal.readMessage(com.kcibald.services.core.proto.DescribeRegionResponse.Success.Companion))
        16 -> responseType =
            DescribeRegionResponse.ResponseType.Failure(protoUnmarshal.readEnum(com.kcibald.services.core.proto.Failure.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun DescribeRegionResponse.Success.protoMergeImpl(plus: DescribeRegionResponse.Success?): DescribeRegionResponse.Success = plus?.copy(
    regionInfo = regionInfo?.plus(plus.regionInfo) ?: plus.regionInfo,
    topPosts = topPosts + plus.topPosts,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun DescribeRegionResponse.Success.protoSizeImpl(): Int {
    var protoSize = 0
    if (regionInfo != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(regionInfo)
    if (topPosts.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * topPosts.size) + topPosts.sumBy(pbandk.Sizer::messageSize)
    if (queryMark.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(queryMark)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DescribeRegionResponse.Success.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (regionInfo != null) protoMarshal.writeTag(10).writeMessage(regionInfo)
    if (topPosts.isNotEmpty()) topPosts.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (queryMark.isNotEmpty()) protoMarshal.writeTag(26).writeString(queryMark)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DescribeRegionResponse.Success.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DescribeRegionResponse.Success {
    var regionInfo: com.kcibald.services.core.proto.RegionInfo? = null
    var topPosts: pbandk.ListWithSize.Builder<com.kcibald.services.core.proto.PostHead>? = null
    var queryMark = ""
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DescribeRegionResponse.Success(regionInfo, pbandk.ListWithSize.Builder.fixed(topPosts), queryMark, protoUnmarshal.unknownFields())
        10 -> regionInfo = protoUnmarshal.readMessage(com.kcibald.services.core.proto.RegionInfo.Companion)
        18 -> topPosts = protoUnmarshal.readRepeatedMessage(topPosts, com.kcibald.services.core.proto.PostHead.Companion, true)
        26 -> queryMark = protoUnmarshal.readString()
        else -> protoUnmarshal.unknownField()
    }
}
