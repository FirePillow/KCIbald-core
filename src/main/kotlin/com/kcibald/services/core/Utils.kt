package com.kcibald.services.core

data class QueryResults<T>(
    val result: List<T>,
    val queryMark: String
)
