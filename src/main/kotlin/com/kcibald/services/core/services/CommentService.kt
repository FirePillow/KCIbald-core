package com.kcibald.services.core.services

import com.kcibald.services.core.QueryResults
import com.kcibald.services.core.proto.CommentInfo
import com.kcibald.services.core.proto.QueryConfig

interface CommentService : Service {

    suspend fun listCommentsUnderPost(
        regionId: String,
        postId: String,
        queryConfig: QueryConfig
    ): QueryResults<CommentInfo>?

}