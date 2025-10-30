package ru.alex3koval.docGenerator.domain.repository.contract.dto

import java.time.LocalDateTime

interface TimestampDTO {
    val createdAt: LocalDateTime
    val updatedAt: LocalDateTime
}
