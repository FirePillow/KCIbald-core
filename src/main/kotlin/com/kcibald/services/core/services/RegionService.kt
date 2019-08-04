package com.kcibald.services.core.services

import com.kcibald.services.core.proto.RegionInfo
import com.kcibald.services.core.services.impl.RegionServiceImpl
import com.kcibald.services.kcibald.URLKey
import com.uchuhimo.konf.Config
import io.vertx.core.Vertx

interface RegionService: Service {

    suspend fun getRegionById(id: String): RegionInfo?
    suspend fun getRegionByUrlKey(urlKey: URLKey): RegionInfo?
    suspend fun translateURLKeyToId(): String?

}