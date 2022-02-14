package Bulletinboard.repository

import Bulletinboard.model.PasswordResets
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PasswordResets: JpaRepository<PasswordResets, Int> {
}