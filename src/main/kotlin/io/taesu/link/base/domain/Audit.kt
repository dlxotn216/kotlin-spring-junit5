package io.taesu.link.base.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EntityListeners

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Embeddable
@EntityListeners(value = [AuditingEntityListener::class])
class Audit(
        @Column(name = "deleted", nullable = false)
        var deleted: Boolean = false,

        @Column(name = "reason", length = 1000, nullable = false)
        var reason: String = "Initial Input",

        @CreatedBy
        @Column(name = "created_by", updatable = false, nullable = false)
        var createdBy: Long = -1L,

        @CreatedDate
        @Column(name = "created_at", updatable = false, nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @LastModifiedBy
        @Column(name = "modified_by")
        var modifiedBy: Long = -1L,

        @LastModifiedDate
        @Column(name = "modified_at")
        var modifiedAt: LocalDateTime = LocalDateTime.now()
): Serializable {
        companion object {
                const val serialVersionUID = 1L
        }
}