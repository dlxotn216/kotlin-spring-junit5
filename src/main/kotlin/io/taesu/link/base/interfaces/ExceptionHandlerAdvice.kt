package io.taesu.link.base.interfaces

import io.taesu.link.base.exception.UnCaughtableException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@RestController
@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(UnCaughtableException::class)
    fun handleException(e: UnCaughtableException): ResponseEntity<FailResponse> = FailResponse.from(e)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<FailResponse> = FailResponse.from(e)
}