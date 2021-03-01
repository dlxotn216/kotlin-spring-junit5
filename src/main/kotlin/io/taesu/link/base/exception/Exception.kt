package io.taesu.link.base.exception

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
data class UnCaughtableException(
        val errorCode: String,
        override val message: String
) : RuntimeException(message) {
    companion object {
        fun doThrow(errorCode: String, message: String): Nothing = throw UnCaughtableException(errorCode, message)
    }
}