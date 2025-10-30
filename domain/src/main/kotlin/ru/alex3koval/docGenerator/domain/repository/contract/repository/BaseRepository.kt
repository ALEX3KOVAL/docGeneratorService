package ru.alex3koval.docGenerator.domain.repository.contract.repository

import reactor.core.publisher.Mono

interface BaseRepository<ID, CREATION_DTO, UPDATING_DTO, RDTO> {
    fun get(id: ID): Mono<RDTO>
    fun create(dto: CREATION_DTO): Mono<ID>
    fun update(id: ID, dto: UPDATING_DTO): Mono<ID>
}
