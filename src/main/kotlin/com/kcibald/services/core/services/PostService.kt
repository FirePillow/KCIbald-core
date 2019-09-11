package com.kcibald.services.core.services

import com.kcibald.services.core.QueryResults
import com.kcibald.services.core.proto.PostHead
import com.kcibald.services.core.proto.PostInfo
import com.kcibald.services.core.proto.QueryConfig
import com.kcibald.services.kcibald.URLKey

interface PostService : Service {

    suspend fun getPostHeadsUnderRegion(regionId: String, queryConfig: QueryConfig): QueryResults<PostHead>?

    suspend fun getPostFromId(regionId: String, postId: String): PostInfo?
    suspend fun getPostFromUrlKey(regionId: String, postURLKey: URLKey): PostInfo?

    suspend fun translateURLKeyToId(urlKey: URLKey): String?

}
