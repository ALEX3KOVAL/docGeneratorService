package ru.alex3koval.docGenerator.storage.repository.impl

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.data.relational.core.sql.SqlIdentifier
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.common.repository.EventRepository
import ru.alex3koval.docGenerator.domain.common.repository.dto.EventRDTO
import ru.alex3koval.docGenerator.domain.common.vo.Topic
import ru.alex3koval.docGenerator.storage.entity.TransactionalOutbox
import ru.alex3koval.docGenerator.storage.repository.orm.OrmEventRepository
import ru.alex3koval.eventingContract.dto.CreateEventWDTO
import ru.alex3koval.eventingContract.dto.UpdateEventWDTO
import ru.alex3koval.eventingContract.vo.EventStatus
import java.time.LocalDateTime
import java.time.ZoneOffset

class TransactionalOutboxRepositoryImpl<ID : Any>(
    private val ormRepository: OrmEventRepository<ID>,
    private val template: R2dbcEntityTemplate
) : EventRepository<ID> {
    override fun get(id: ID): Mono<EventRDTO> = ormRepository
        .findById(id)
        .mapNotNull { entity -> entity.toRdto() }

    override fun add(dto: CreateEventWDTO): Mono<ID> = ormRepository
        .saveWithReturning(dto.toEntity())
        .mapNotNull { entity -> entity.id }

    override fun update(
        id: ID,
        dto: UpdateEventWDTO
    ): Mono<ID> {
        val fieldsForUpdating: MutableMap<SqlIdentifier?, Any?> = hashMapOf()

        if (dto.status != null) {
            fieldsForUpdating.put(SqlIdentifier.quoted("status"), dto.status)
        }

        if (fieldsForUpdating.isEmpty()) {
            return Mono.just<ID>(id)
        }

        fieldsForUpdating.put(
            SqlIdentifier.quoted("updated_at"),
            LocalDateTime.now(ZoneOffset.UTC)
        )

        val query = Query.query(
            Criteria.where("id").`is`(id)
        )

        return template
            .update(TransactionalOutbox::class.java)
            .matching(query)
            .apply(Update.from(fieldsForUpdating))
            .thenReturn<ID>(id)
    }

    override fun updateStatus(
        id: ID,
        newStatus: EventStatus
    ): Mono<ID>  = update(id, UpdateEventWDTO(newStatus))

    private fun TransactionalOutbox<ID>.toRdto() = EventRDTO(
        name = name,
        topic = Topic(topic).getOrThrow(),
        json = json,
        status = status,
        createdAt = createdAt
    )

    private fun CreateEventWDTO.toEntity(): TransactionalOutbox<ID> = TransactionalOutbox(
        name = name,
        topic = topic,
        status = status,
        json = json,
        createdAt = createdAt,
        updatedAt = createdAt
    )
}
