package com.kcibald.services.core.proto

data class DescribePostRequest(
    val region: Region? = null,
    val post: Post? = null,
    val queryConfig: com.kcibald.services.core.proto.QueryConfig? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<DescribePostRequest> {
    sealed class Region {
        data class RId(val rId: String = "") : Region()
        data class RUrlKey(val rUrlKey: String = "") : Region()
    }

    sealed class Post {
        data class PId(val pId: String = "") : Post()
        data class PUrlKey(val pUrlKey: String = "") : Post()
    }

    override operator fun plus(other: DescribePostRequest?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<DescribePostRequest> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = DescribePostRequest.protoUnmarshalImpl(u)
    }
}

data class DescribePostResponse(
    val responseType: ResponseType? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<DescribePostResponse> {
    sealed class ResponseType {
        data class Success(val success: com.kcibald.services.core.proto.DescribePostResponse.Success) : ResponseType()
        data class Failure(
            val failure: com.kcibald.services.core.proto.DescribePostResponse.Failure = com.kcibald.services.core.proto.DescribePostResponse.Failure.fromValue(
                0
            )
        ) : ResponseType()
    }

    override operator fun plus(other: DescribePostResponse?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<DescribePostResponse> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = DescribePostResponse.protoUnmarshalImpl(u)
    }

    data class Failure(override val value: Int) : pbandk.Message.Enum {
        companion object : pbandk.Message.Enum.Companion<Failure> {
            val NOT_FOUND = Failure(0)
            val ERROR = Failure(1)

            override fun fromValue(value: Int) = when (value) {
                0 -> NOT_FOUND
                1 -> ERROR
                else -> Failure(value)
            }
        }
    }

    data class Success(
        val postInfo: com.kcibald.services.core.proto.PostInfo? = null,
        val comment: List<com.kcibald.services.core.proto.CommentInfo> = emptyList(),
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

private fun DescribePostRequest.protoMergeImpl(plus: DescribePostRequest?): DescribePostRequest = plus?.copy(
    region = plus.region ?: region,
    post = plus.post ?: post,
    queryConfig = queryConfig?.plus(plus.queryConfig) ?: plus.queryConfig,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun DescribePostRequest.protoSizeImpl(): Int {
    var protoSize = 0
    when (region) {
        is DescribePostRequest.Region.RId -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(region.rId)
        is DescribePostRequest.Region.RUrlKey -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(region.rUrlKey)
    }
    when (post) {
        is DescribePostRequest.Post.PId -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(post.pId)
        is DescribePostRequest.Post.PUrlKey -> protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.stringSize(post.pUrlKey)
    }
    if (queryConfig != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(queryConfig)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DescribePostRequest.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (region is DescribePostRequest.Region.RId) protoMarshal.writeTag(10).writeString(region.rId)
    if (region is DescribePostRequest.Region.RUrlKey) protoMarshal.writeTag(18).writeString(region.rUrlKey)
    if (post is DescribePostRequest.Post.PId) protoMarshal.writeTag(26).writeString(post.pId)
    if (post is DescribePostRequest.Post.PUrlKey) protoMarshal.writeTag(34).writeString(post.pUrlKey)
    if (queryConfig != null) protoMarshal.writeTag(42).writeMessage(queryConfig)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DescribePostRequest.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DescribePostRequest {
    var region: DescribePostRequest.Region? = null
    var post: DescribePostRequest.Post? = null
    var queryConfig: com.kcibald.services.core.proto.QueryConfig? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DescribePostRequest(region, post, queryConfig, protoUnmarshal.unknownFields())
        10 -> region = DescribePostRequest.Region.RId(protoUnmarshal.readString())
        18 -> region = DescribePostRequest.Region.RUrlKey(protoUnmarshal.readString())
        26 -> post = DescribePostRequest.Post.PId(protoUnmarshal.readString())
        34 -> post = DescribePostRequest.Post.PUrlKey(protoUnmarshal.readString())
        42 -> queryConfig = protoUnmarshal.readMessage(com.kcibald.services.core.proto.QueryConfig.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun DescribePostResponse.protoMergeImpl(plus: DescribePostResponse?): DescribePostResponse = plus?.copy(
    responseType = when {
        responseType is DescribePostResponse.ResponseType.Success && plus.responseType is DescribePostResponse.ResponseType.Success ->
            DescribePostResponse.ResponseType.Success(responseType.success + plus.responseType.success)
        else ->
            plus.responseType ?: responseType
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun DescribePostResponse.protoSizeImpl(): Int {
    var protoSize = 0
    when (responseType) {
        is DescribePostResponse.ResponseType.Success -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(
            responseType.success
        )
        is DescribePostResponse.ResponseType.Failure -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(
            responseType.failure
        )
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DescribePostResponse.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (responseType is DescribePostResponse.ResponseType.Success) protoMarshal.writeTag(10).writeMessage(responseType.success)
    if (responseType is DescribePostResponse.ResponseType.Failure) protoMarshal.writeTag(16).writeEnum(responseType.failure)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DescribePostResponse.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DescribePostResponse {
    var responseType: DescribePostResponse.ResponseType? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DescribePostResponse(responseType, protoUnmarshal.unknownFields())
        10 -> responseType =
            DescribePostResponse.ResponseType.Success(protoUnmarshal.readMessage(com.kcibald.services.core.proto.DescribePostResponse.Success.Companion))
        16 -> responseType =
            DescribePostResponse.ResponseType.Failure(protoUnmarshal.readEnum(com.kcibald.services.core.proto.DescribePostResponse.Failure.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun DescribePostResponse.Success.protoMergeImpl(plus: DescribePostResponse.Success?): DescribePostResponse.Success =
    plus?.copy(
        postInfo = postInfo?.plus(plus.postInfo) ?: plus.postInfo,
        comment = comment + plus.comment,
        unknownFields = unknownFields + plus.unknownFields
    ) ?: this

private fun DescribePostResponse.Success.protoSizeImpl(): Int {
    var protoSize = 0
    if (postInfo != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(postInfo)
    if (comment.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * comment.size) + comment.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DescribePostResponse.Success.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (postInfo != null) protoMarshal.writeTag(10).writeMessage(postInfo)
    if (comment.isNotEmpty()) comment.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DescribePostResponse.Success.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DescribePostResponse.Success {
    var postInfo: com.kcibald.services.core.proto.PostInfo? = null
    var comment: pbandk.ListWithSize.Builder<com.kcibald.services.core.proto.CommentInfo>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DescribePostResponse.Success(
            postInfo,
            pbandk.ListWithSize.Builder.fixed(comment),
            protoUnmarshal.unknownFields()
        )
        10 -> postInfo = protoUnmarshal.readMessage(com.kcibald.services.core.proto.PostInfo.Companion)
        18 -> comment =
            protoUnmarshal.readRepeatedMessage(comment, com.kcibald.services.core.proto.CommentInfo.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}
