package Bulletinboard.model

import org.hibernate.annotations.BatchSize
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(unique = true, nullable = false)
    var name: String? = null,

    @Column(unique = true, nullable = false)
    var email: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    var profile: String? = null,

    val type: Boolean = true,

    @get: Size(max = 20)
    var phone: String? = null,

    val address: String? = null,

    val dob: Date? = null,

    @Column(name = "created_user_id", nullable = false)
    var createdUserId: Int = 0,

    @Column(name = "updated_user_id", nullable = false)
    var updatedUserId: Int = 0,

    @Column(name = "deleted_user_id")
    val deletedUserId: Int? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null,


    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "name")]
    )
    @BatchSize(size = 20)
    var authorities: MutableSet<Authority> = mutableSetOf(),
) {
    override fun toString(): String {
        return "User(username='$name', password='$password', email=$email, authorities=$authorities, createdBy=$createdUserId, createdDate=$createdAt, lastModifiedBy=$updatedUserId, lastModifiedDate=$updatedAt, id=$id)"
    }
}
