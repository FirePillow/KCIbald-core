package com.kcibald.services.core.services

interface Service {

    suspend fun start()
    suspend fun stop()

}