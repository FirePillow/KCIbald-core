package com.kcibald.services.core

data class QueryResult<T>(
    val result: T,
    val queryMark: String
)
