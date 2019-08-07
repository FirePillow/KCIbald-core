package com.kcibald.services.core.proto

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

data class QueryConfig(
    val marker: String = "",
    val amount: Int = 0,
    val skip: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<QueryConfig> {
    override operator fun plus(other: QueryConfig?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<QueryConfig> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = QueryConfig.protoUnmarshalImpl(u)
    }
}

data class RegionInfo(
    val id: String = "",
    val urlKey: String = "",
    val name: String = "",
    val parent: com.kcibald.services.core.proto.RegionInfo? = null,
    val description: String = "",
    val avatar: String = "",
    val colorLeft: String = "",
    val colorRight: String = "",
    val children: List<com.kcibald.services.core.proto.RegionInfo> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<RegionInfo> {
    override operator fun plus(other: RegionInfo?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<RegionInfo> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = RegionInfo.protoUnmarshalImpl(u)
    }
}

data class PostHead(
    val urlKey: String = "",
    val title: String = "",
    val author: String = "",
    val creationTimeStamp: Long = 0L,
    val content: String = "",
    val commentCount: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<PostHead> {
    override operator fun plus(other: PostHead?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<PostHead> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = PostHead.protoUnmarshalImpl(u)
    }
}

data class PostInfo(
    val urlKey: String = "",
    val title: String = "",
    val author: String = "",
    val creationTimeStamp: Long = 0L,
    val content: String = "",
    val commentCount: Int = 0,
    val attachments: List<com.kcibald.services.core.proto.NamedAttachment> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<PostInfo> {
    override operator fun plus(other: PostInfo?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<PostInfo> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = PostInfo.protoUnmarshalImpl(u)
    }
}

data class CommentInfo(
    val id: Int = 0,
    val author: String = "",
    val creationTimeStamp: Long = 0L,
    val content: String = "",
    val replies: List<com.kcibald.services.core.proto.CommentInfo> = emptyList(),
    val attachments: List<com.kcibald.services.core.proto.NamedAttachment> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<CommentInfo> {
    override operator fun plus(other: CommentInfo?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<CommentInfo> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = CommentInfo.protoUnmarshalImpl(u)
    }
}

data class NamedAttachment(
    val url: String = "",
    val title: String = "",
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<NamedAttachment> {
    override operator fun plus(other: NamedAttachment?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<NamedAttachment> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = NamedAttachment.protoUnmarshalImpl(u)
    }
}

private fun QueryConfig.protoMergeImpl(plus: QueryConfig?): QueryConfig = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun QueryConfig.protoSizeImpl(): Int {
    var protoSize = 0
    if (marker.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(marker)
    if (amount != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(amount)
    if (skip != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(skip)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun QueryConfig.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (marker.isNotEmpty()) protoMarshal.writeTag(10).writeString(marker)
    if (amount != 0) protoMarshal.writeTag(16).writeInt32(amount)
    if (skip != 0) protoMarshal.writeTag(24).writeInt32(skip)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun QueryConfig.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): QueryConfig {
    var marker = ""
    var amount = 0
    var skip = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return QueryConfig(marker, amount, skip, protoUnmarshal.unknownFields())
        10 -> marker = protoUnmarshal.readString()
        16 -> amount = protoUnmarshal.readInt32()
        24 -> skip = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun RegionInfo.protoMergeImpl(plus: RegionInfo?): RegionInfo = plus?.copy(
    parent = parent?.plus(plus.parent) ?: plus.parent,
    children = children + plus.children,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun RegionInfo.protoSizeImpl(): Int {
    var protoSize = 0
    if (id.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(id)
    if (urlKey.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(urlKey)
    if (name.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(name)
    if (parent != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(parent)
    if (description.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.stringSize(description)
    if (avatar.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.stringSize(avatar)
    if (colorLeft.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(7) + pbandk.Sizer.stringSize(colorLeft)
    if (colorRight.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(8) + pbandk.Sizer.stringSize(colorRight)
    if (children.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(9) * children.size) + children.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun RegionInfo.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (id.isNotEmpty()) protoMarshal.writeTag(10).writeString(id)
    if (urlKey.isNotEmpty()) protoMarshal.writeTag(18).writeString(urlKey)
    if (name.isNotEmpty()) protoMarshal.writeTag(26).writeString(name)
    if (parent != null) protoMarshal.writeTag(34).writeMessage(parent)
    if (description.isNotEmpty()) protoMarshal.writeTag(42).writeString(description)
    if (avatar.isNotEmpty()) protoMarshal.writeTag(50).writeString(avatar)
    if (colorLeft.isNotEmpty()) protoMarshal.writeTag(58).writeString(colorLeft)
    if (colorRight.isNotEmpty()) protoMarshal.writeTag(66).writeString(colorRight)
    if (children.isNotEmpty()) children.forEach { protoMarshal.writeTag(74).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun RegionInfo.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): RegionInfo {
    var id = ""
    var urlKey = ""
    var name = ""
    var parent: com.kcibald.services.core.proto.RegionInfo? = null
    var description = ""
    var avatar = ""
    var colorLeft = ""
    var colorRight = ""
    var children: pbandk.ListWithSize.Builder<com.kcibald.services.core.proto.RegionInfo>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return RegionInfo(id, urlKey, name, parent,
            description, avatar, colorLeft, colorRight,
            pbandk.ListWithSize.Builder.fixed(children), protoUnmarshal.unknownFields())
        10 -> id = protoUnmarshal.readString()
        18 -> urlKey = protoUnmarshal.readString()
        26 -> name = protoUnmarshal.readString()
        34 -> parent = protoUnmarshal.readMessage(com.kcibald.services.core.proto.RegionInfo.Companion)
        42 -> description = protoUnmarshal.readString()
        50 -> avatar = protoUnmarshal.readString()
        58 -> colorLeft = protoUnmarshal.readString()
        66 -> colorRight = protoUnmarshal.readString()
        74 -> children = protoUnmarshal.readRepeatedMessage(children, com.kcibald.services.core.proto.RegionInfo.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun PostHead.protoMergeImpl(plus: PostHead?): PostHead = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun PostHead.protoSizeImpl(): Int {
    var protoSize = 0
    if (urlKey.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(urlKey)
    if (title.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(title)
    if (author.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(author)
    if (creationTimeStamp != 0L) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int64Size(creationTimeStamp)
    if (content.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.stringSize(content)
    if (commentCount != 0) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.int32Size(commentCount)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun PostHead.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (urlKey.isNotEmpty()) protoMarshal.writeTag(10).writeString(urlKey)
    if (title.isNotEmpty()) protoMarshal.writeTag(18).writeString(title)
    if (author.isNotEmpty()) protoMarshal.writeTag(26).writeString(author)
    if (creationTimeStamp != 0L) protoMarshal.writeTag(32).writeInt64(creationTimeStamp)
    if (content.isNotEmpty()) protoMarshal.writeTag(42).writeString(content)
    if (commentCount != 0) protoMarshal.writeTag(48).writeInt32(commentCount)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun PostHead.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): PostHead {
    var urlKey = ""
    var title = ""
    var author = ""
    var creationTimeStamp = 0L
    var content = ""
    var commentCount = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return PostHead(urlKey, title, author, creationTimeStamp,
            content, commentCount, protoUnmarshal.unknownFields())
        10 -> urlKey = protoUnmarshal.readString()
        18 -> title = protoUnmarshal.readString()
        26 -> author = protoUnmarshal.readString()
        32 -> creationTimeStamp = protoUnmarshal.readInt64()
        42 -> content = protoUnmarshal.readString()
        48 -> commentCount = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun PostInfo.protoMergeImpl(plus: PostInfo?): PostInfo = plus?.copy(
    attachments = attachments + plus.attachments,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun PostInfo.protoSizeImpl(): Int {
    var protoSize = 0
    if (urlKey.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(urlKey)
    if (title.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(title)
    if (author.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(author)
    if (creationTimeStamp != 0L) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int64Size(creationTimeStamp)
    if (content.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.stringSize(content)
    if (commentCount != 0) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.int32Size(commentCount)
    if (attachments.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(7) * attachments.size) + attachments.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun PostInfo.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (urlKey.isNotEmpty()) protoMarshal.writeTag(10).writeString(urlKey)
    if (title.isNotEmpty()) protoMarshal.writeTag(18).writeString(title)
    if (author.isNotEmpty()) protoMarshal.writeTag(26).writeString(author)
    if (creationTimeStamp != 0L) protoMarshal.writeTag(32).writeInt64(creationTimeStamp)
    if (content.isNotEmpty()) protoMarshal.writeTag(42).writeString(content)
    if (commentCount != 0) protoMarshal.writeTag(48).writeInt32(commentCount)
    if (attachments.isNotEmpty()) attachments.forEach { protoMarshal.writeTag(58).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun PostInfo.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): PostInfo {
    var urlKey = ""
    var title = ""
    var author = ""
    var creationTimeStamp = 0L
    var content = ""
    var commentCount = 0
    var attachments: pbandk.ListWithSize.Builder<com.kcibald.services.core.proto.NamedAttachment>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return PostInfo(urlKey, title, author, creationTimeStamp,
            content, commentCount, pbandk.ListWithSize.Builder.fixed(attachments), protoUnmarshal.unknownFields())
        10 -> urlKey = protoUnmarshal.readString()
        18 -> title = protoUnmarshal.readString()
        26 -> author = protoUnmarshal.readString()
        32 -> creationTimeStamp = protoUnmarshal.readInt64()
        42 -> content = protoUnmarshal.readString()
        48 -> commentCount = protoUnmarshal.readInt32()
        58 -> attachments = protoUnmarshal.readRepeatedMessage(attachments, com.kcibald.services.core.proto.NamedAttachment.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun CommentInfo.protoMergeImpl(plus: CommentInfo?): CommentInfo = plus?.copy(
    replies = replies + plus.replies,
    attachments = attachments + plus.attachments,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CommentInfo.protoSizeImpl(): Int {
    var protoSize = 0
    if (id != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(id)
    if (author.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(author)
    if (creationTimeStamp != 0L) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int64Size(creationTimeStamp)
    if (content.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.stringSize(content)
    if (replies.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(5) * replies.size) + replies.sumBy(pbandk.Sizer::messageSize)
    if (attachments.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(6) * attachments.size) + attachments.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CommentInfo.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (id != 0) protoMarshal.writeTag(8).writeInt32(id)
    if (author.isNotEmpty()) protoMarshal.writeTag(18).writeString(author)
    if (creationTimeStamp != 0L) protoMarshal.writeTag(24).writeInt64(creationTimeStamp)
    if (content.isNotEmpty()) protoMarshal.writeTag(34).writeString(content)
    if (replies.isNotEmpty()) replies.forEach { protoMarshal.writeTag(42).writeMessage(it) }
    if (attachments.isNotEmpty()) attachments.forEach { protoMarshal.writeTag(50).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CommentInfo.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CommentInfo {
    var id = 0
    var author = ""
    var creationTimeStamp = 0L
    var content = ""
    var replies: pbandk.ListWithSize.Builder<com.kcibald.services.core.proto.CommentInfo>? = null
    var attachments: pbandk.ListWithSize.Builder<com.kcibald.services.core.proto.NamedAttachment>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CommentInfo(id, author, creationTimeStamp, content,
            pbandk.ListWithSize.Builder.fixed(replies), pbandk.ListWithSize.Builder.fixed(attachments), protoUnmarshal.unknownFields())
        8 -> id = protoUnmarshal.readInt32()
        18 -> author = protoUnmarshal.readString()
        24 -> creationTimeStamp = protoUnmarshal.readInt64()
        34 -> content = protoUnmarshal.readString()
        42 -> replies = protoUnmarshal.readRepeatedMessage(replies, com.kcibald.services.core.proto.CommentInfo.Companion, true)
        50 -> attachments = protoUnmarshal.readRepeatedMessage(attachments, com.kcibald.services.core.proto.NamedAttachment.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun NamedAttachment.protoMergeImpl(plus: NamedAttachment?): NamedAttachment = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun NamedAttachment.protoSizeImpl(): Int {
    var protoSize = 0
    if (url.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(url)
    if (title.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(title)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun NamedAttachment.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (url.isNotEmpty()) protoMarshal.writeTag(10).writeString(url)
    if (title.isNotEmpty()) protoMarshal.writeTag(18).writeString(title)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun NamedAttachment.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): NamedAttachment {
    var url = ""
    var title = ""
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return NamedAttachment(url, title, protoUnmarshal.unknownFields())
        10 -> url = protoUnmarshal.readString()
        18 -> title = protoUnmarshal.readString()
        else -> protoUnmarshal.unknownField()
    }
}
