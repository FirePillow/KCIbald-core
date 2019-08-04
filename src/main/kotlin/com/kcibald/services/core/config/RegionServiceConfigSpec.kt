package com.kcibald.services.core.config

import com.uchuhimo.konf.ConfigSpec

object RegionServiceConfigSpec : ConfigSpec("region") {
    val mongoConfig by required<Map<String, Any>>("mongo_config")
    val collectionName by required<String>("collection_name")
    val eventBusAddr by required<String>("eventbus_address")
}