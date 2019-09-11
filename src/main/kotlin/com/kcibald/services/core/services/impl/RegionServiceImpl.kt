package com.kcibald.services.core.services.impl

import com.kcibald.services.core.config.RegionServiceConfigSpec
import com.kcibald.services.core.proto.RegionInfo
import com.kcibald.services.core.services.RegionService
import com.kcibald.services.kcibald.URLKey
import com.kcibald.utils.IDUtil
import com.uchuhimo.konf.Config
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.ext.mongo.findOneAwait

class RegionServiceImpl(
    vertx: Vertx,
    config: Config
) : RegionService {

    internal val mongoClient = MongoClient.createShared(vertx, JsonObject(config[RegionServiceConfigSpec.mongoConfig]))
    internal val collectionName = config[RegionServiceConfigSpec.collectionName]

    private val idKey = "_id"
    internal val urlKeyKey = "urlKey"
    internal val nameKey = "name"
    internal val descriptionKey = "description"
    internal val avatarKey = "avatar"
    internal val colorKey = "color"
    internal val colorLeftKey = "left"
    internal val colorRightKey = "right"

    private val fields = jsonObjectOf(
        idKey to 1,
        urlKeyKey to 1,
        nameKey to 1,
        descriptionKey to 1,
        avatarKey to 1,
        "$colorKey.$colorLeftKey" to 1,
        "$colorKey.$colorRightKey" to 1
    )

    private fun JsonObject.toRegionInfo(): RegionInfo = RegionInfo(
        id = IDUtil.encodeDBID(this.getString(idKey)),
        urlKey = this.getString(urlKeyKey),
        name = this.getString(nameKey),
        description = this.getString(descriptionKey),
        avatar = this.getString(avatarKey),
        colorLeft = this.getJsonObject("color").getString("left"),
        colorRight = this.getJsonObject("color").getString("right")
    )

    override suspend fun getRegionById(id: String): RegionInfo? {
        val query = jsonObjectOf(
            idKey to IDUtil.decodeDBID(id)
        )
        return mongoClient
            .findOneAwait(collectionName, query, fields)
            ?.toRegionInfo()
    }

    override suspend fun getRegionByUrlKey(urlKey: URLKey): RegionInfo? {
        val query = jsonObjectOf(
            "urlKey" to urlKey
        )

        return mongoClient
            .findOneAwait(collectionName, query, fields)
            ?.toRegionInfo()
    }

    override suspend fun translateURLKeyToId(urlKey: URLKey): String? {
//        TODO: Caching
        val query = jsonObjectOf(
            urlKeyKey to urlKey
        )
        val fields = jsonObjectOf(
            idKey to 1
        )

        val dbJson = mongoClient.findOneAwait(collectionName, query, fields) ?: return null

        return IDUtil.encodeDBID(dbJson.getString(idKey))
    }

    override suspend fun start() {}

    override suspend fun stop() {
        mongoClient.close()
    }

}