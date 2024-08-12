package com.sycosoft.jkc.database.result

/** Provides the result of a database operation.
 *
 * @see DatabaseResultCode
 * @property code The [DatabaseResultCode] of the operation.
 * @property entity The entity to be returned by the operation. This can be null.
 * @property errorMessage If an error occurred, this will contain the reason. Otherwise, it is null.
 */
data class DatabaseResult<T>(
    val code: DatabaseResultCode,
    val entity: T? = null,
    val errorMessage: String? = null,
)