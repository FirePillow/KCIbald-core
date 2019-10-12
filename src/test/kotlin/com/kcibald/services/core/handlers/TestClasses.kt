package com.kcibald.services.core.handlers

import com.kcibald.services.core.QueryResults
import com.kcibald.services.core.proto.*
import com.kcibald.services.core.services.CommentService
import com.kcibald.services.core.services.PostService
import com.kcibald.services.core.services.RegionService
import com.kcibald.services.kcibald.URLKey

internal open class TestRegionService : RegionService {
    override suspend fun getRegionById(id: String): RegionInfo? = throw AssertionError()

    override suspend fun getRegionByUrlKey(urlKey: URLKey): RegionInfo? = throw AssertionError()

    override suspend fun translateURLKeyToId(urlKey: URLKey): String? = throw AssertionError()

    override suspend fun start() {}

    override suspend fun stop() {}
}

internal open class TestPostService : PostService {
    override suspend fun getPostFromId(regionId: String, postId: String): PostInfo? = throw AssertionError()

    override suspend fun getPostFromUrlKey(regionId: String, postURLKey: URLKey): PostInfo? = throw AssertionError()

    override suspend fun translateURLKeyToId(urlKey: URLKey): String? = throw AssertionError()

    override suspend fun getPostHeadsUnderRegion(
        regionId: String,
        queryConfig: QueryConfig
    ): QueryResults<PostHead>? {
        throw AssertionError()
    }

    override suspend fun start() {}

    override suspend fun stop() {}
}

internal open class TestCommentService : CommentService {
    override suspend fun listCommentsUnderPost(
        regionId: String,
        postId: String,
        queryConfig: QueryConfig
    ): QueryResults<CommentInfo>? = throw AssertionError()

    override suspend fun start() {}

    override suspend fun stop() {}
}