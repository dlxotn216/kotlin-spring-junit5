package io.crscube.link.shorteener.domain

import io.crscube.link.base.domain.Audit
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Table(name = "USR_LINK")
@Entity
@Audited
@EntityListeners(value = [AuditingEntityListener::class])
class ShortenLink(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USR_LINK_SEQ")
        @SequenceGenerator(name = "USR_LINK_SEQ", sequenceName = "USR_LINK_SEQ")
        var key: Long? = null,

        @Lob @Column(name = "ORIGIN_LINK", nullable = false)
        val originLink: String,

        @Column(name = "HASH")
        var hash: String = "",

        @Column(name = "EXPIRATION_DURATION", nullable = true)
        val expirationDuration: Duration? = null,

        @Embedded
        val audit: Audit = Audit()
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }

    val createdAt: LocalDateTime
        get() = audit.createdAt

    val expiredAt: LocalDateTime?
        get() = expirationDuration?.let { createdAt.plus(expirationDuration) }

    override fun equals(other: Any?): Boolean {
        if (other !is ShortenLink) {
            return false
        }

        if (other == this) {
            return true
        }

        return this.key != null && other.key != null && this.key == other.key
    }

    override fun hashCode(): Int {
        return this.key?.hashCode() ?: 31
    }
}

interface ShortenLinkRepository : JpaRepository<ShortenLink, Long> {
    fun findByHash(hash: String): ShortenLink?
}

