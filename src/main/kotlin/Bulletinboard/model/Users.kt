package Bulletinboard.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(unique = true, nullable = false)
    var name: String? = null,

    @Column(unique = true, nullable = false)
    var email: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    @Column(nullable = false)
    var profile: String? = null,

    @Column(nullable = false)
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

    @ManyToOne
    @JoinColumn(name = "created_user_id", insertable = false, updatable = false)
    val user: Users? = null,
//    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
//    val posts: List<Posts>
)
