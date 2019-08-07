package com.kcibald.services.core.services

import com.kcibald.services.core.proto.RegionInfo
import com.kcibald.services.kcibald.URLKey

interface RegionService: Service {

    suspend fun getRegionById(id: String): RegionInfo?
    suspend fun getRegionByUrlKey(urlKey: URLKey): RegionInfo?
    suspend fun translateURLKeyToId(urlKey: URLKey): String?

}