package Bulletinboard.model

import org.hibernate.validator.constraints.UniqueElements
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
data class Posts(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @NotNull(message = "The title is required.")
    @Column(unique = true, nullable = false)
    var title: String? = null,

    @Column(nullable = false)
    var description: String? = null,

    @Column(nullable = false)
    val status: Boolean = true,

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
    @JoinColumn(name = "updated_user_id", insertable = false, updatable = false)
    val updatedUser: Users? = null,

    @ManyToOne
    @JoinColumn(name = "created_user_id", insertable = false, updatable = false)
    val user: Users? = null,
)

