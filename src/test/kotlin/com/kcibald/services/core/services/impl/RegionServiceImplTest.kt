package com.kcibald.services.core.services.impl

import com.kcibald.services.core.config.RegionServiceConfigSpec
import com.kcibald.services.core.proto.RegionInfo
import com.kcibald.utils.IDUtil
import com.uchuhimo.konf.Config
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.ext.mongo.createCollectionAwait
import io.vertx.kotlin.ext.mongo.dropCollectionAwait
import io.vertx.kotlin.ext.mongo.insertAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(VertxExtension::class)
internal class RegionServiceImplTest {

    private lateinit var target: RegionServiceImpl

    @BeforeEach
    fun setup(vertx: Vertx) = runBlocking {
        val config = Config { addSpec(RegionServiceConfigSpec) }
            .from.json.resource("region-service-impl-default.json")

        println(config)

        target = RegionServiceImpl(vertx, config)
        target.start()
        target.mongoClient.dropCollectionAwait("lol")
        target.mongoClient.createCollectionAwait("lol")
        createNoise(10)
        Unit
    }

    private suspend fun createNoise(times: Int) {
        val randomArray = ByteArray(20)
        Random().nextBytes(randomArray)
        val randomString = Base64.getEncoder().encodeToString(randomArray)
        for (i in 1..times) {
            insertRandomOne("noise-document", "$randomString-$times")
        }
    }

    private suspend fun insertRandomOne(prefix: String = "test", suffixIn: String = ""): RegionInfo {
        val suffix =
            if (suffixIn.isEmpty()) {
                val holder = ByteArray(20)
                Random().nextBytes(holder)
                Base64.getEncoder().encodeToString(holder)
            } else {
                suffixIn
            }

        val name = "$prefix-$suffix"
        val description = "$prefix-desc-$suffix"
        val avatar = "$prefix-avatar-$suffix"
        val colorLeft = "$prefix-color-left-$suffix"
        val colorRight = "$prefix-color-right-$suffix"

        val region = RegionInfo(
            urlKey = name,
            name = name,
            parent = null,
            description = description,
            avatar = avatar,
            colorLeft = colorLeft,
            colorRight = colorRight,
            children = emptyList()
        )

        return insertRegion(region)
    }

    private suspend fun insertRegion(region: RegionInfo): RegionInfo {
        val document = jsonObjectOf(
            target.urlKeyKey to region.urlKey,
            target.nameKey to region.name,
            target.descriptionKey to region.description,
            target.avatarKey to region.avatar,
            target.colorKey to jsonObjectOf(
                target.colorLeftKey to region.colorLeft,
                target.colorRightKey to region.colorRight
            )
        )

        val insertResult = target.mongoClient.insertAwait(target.collectionName, document)!!
        return region.copy(id = IDUtil.encodeDBID(insertResult))
    }

    @AfterEach
    fun cleanAfterTest() = runBlocking {
        target.mongoClient.dropCollectionAwait("lol")
        target.stop()
    }

    @Test
    fun getRegionById() = runBlocking {
        val region = insertRandomOne()

        val result = target.getRegionById(region.id)

        assertEquals(region, result)

        Unit
    }

    @Test
    fun getRegionById_notfound() = runBlocking {
        val idInBytes = "this-is-not-a-possible-id".toByteArray()

        val result = target.getRegionById(Base64.getEncoder().encodeToString(idInBytes))

        assertNull(result)

        Unit
    }

    @Test
    fun getRegionByUrlKey() = runBlocking {
        val region = insertRandomOne()

        val result = target.getRegionByUrlKey(region.urlKey)

        assertEquals(region, result)

        Unit
    }

    @Test
    fun getRegionUrlKey_notfound() = runBlocking {
        val result = target.getRegionByUrlKey("not_exist")

        assertNull(result)

        Unit
    }

    @Test
    fun translateURLKeyToId() = runBlocking {
        val region = insertRandomOne()

        val result = target.translateURLKeyToId(region.urlKey)

        assertEquals(region.id, result)

        Unit
    }

    @Test
    fun translateURLKeyToId_notFound() = runBlocking {
        val result = target.translateURLKeyToId("not-a-present-url-key")

        assertNull(result)

        Unit
    }

}