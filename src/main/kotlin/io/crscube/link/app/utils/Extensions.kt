package io.crscube.link.app.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
fun LocalDateTime.yyyyMMddhhmmss() = this.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"))