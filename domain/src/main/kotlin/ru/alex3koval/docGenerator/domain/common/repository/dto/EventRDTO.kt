package ru.alex3koval.docGenerator.domain.common.repository.dto

import ru.alex3koval.docGenerator.domain.common.vo.Topic
import ru.alex3koval.eventingContract.vo.EventStatus
import java.time.LocalDateTime

data class EventRDTO(
    val name: String,
    val topic: Topic,
    val json: String,
    val status: EventStatus,
    val createdAt: LocalDateTime
)
