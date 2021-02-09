package io.crscube.link.base.interfaces

import io.crscube.link.app.config.defaultFailureMessage
import io.crscube.link.app.config.defaultLanguage
import io.crscube.link.app.config.defaultSuccessMessage
import io.crscube.link.app.config.defaultTimeZone
import io.crscube.link.base.exception.UnCaughtableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
data class SuccessResponse(
        val result: Any?,
        val timezone: String = defaultTimeZone,
        val language: String = defaultLanguage,
        val message: String = defaultSuccessMessage,
        val status: String = "success"
) {
    companion object {
        fun created(result: Any?) =
                ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse(result))
    }
}

data class FailResponse(
        val errorCode: String,
        val timezone: String = defaultTimeZone,
        val language: String = defaultLanguage,
        val message: String = defaultFailureMessage,
        val status: String = "fail"
) {
    companion object {
        fun from(e: UnCaughtableException): ResponseEntity<FailResponse> =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FailResponse(errorCode = e.errorCode, message = e.message))

        fun from(e: Exception): ResponseEntity<FailResponse> =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FailResponse(errorCode = "UNEXPECTED", message = e.message
                        ?: ""))
    }
}
