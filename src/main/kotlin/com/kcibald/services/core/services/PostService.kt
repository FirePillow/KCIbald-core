package com.kcibald.services.core.services

import com.kcibald.services.core.QueryResults
import com.kcibald.services.core.proto.PostHead
import com.kcibald.services.core.proto.PostInfo
import com.kcibald.services.core.proto.QueryConfig

interface PostService {

    suspend fun getPostHeadsUnderRegion(regionId: String, queryConfig: QueryConfig): QueryResults<PostHead>?

    suspend fun getPostsUnderRegion(regionId: String, queryConfig: QueryConfig): QueryResults<PostInfo>?

}
