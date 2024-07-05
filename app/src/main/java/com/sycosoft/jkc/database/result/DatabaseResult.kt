package com.sycosoft.jkc.database.result

data class DatabaseResult<T>(
    val code: DatabaseResultCode,
    val entity: T? = null,
    val message: String? = null,
)
