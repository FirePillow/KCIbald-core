package com.kcibald.services.core.config

import com.uchuhimo.konf.Config
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RegionServiceConfigSpecTest {

    val config = Config { addSpec(RegionServiceConfigSpec) }
        .from.map.hierarchical(
        mapOf(
            "region" to mapOf(
                "mongo_config" to mapOf("a" to "b"),
                "collection_name" to "collection_name",
                "eventbus_address" to "addr"
            )
        )
    )

    @Test
    fun getMongoConfig() {
        assertEquals(mapOf("a" to "b"), config[RegionServiceConfigSpec.mongoConfig])
    }

    @Test
    fun getCollectionName() {
        assertEquals("collection_name", config[RegionServiceConfigSpec.collectionName])
    }

    @Test
    fun getEventBusAddr() {
        assertEquals("addr", config[RegionServiceConfigSpec.eventBusAddr])
    }

}