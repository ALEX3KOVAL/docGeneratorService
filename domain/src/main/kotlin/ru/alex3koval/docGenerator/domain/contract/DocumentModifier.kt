package ru.alex3koval.docGenerator.domain.contract

interface DocumentModifier<R : Any, DTO : BaseGeneratedDocDTO> {
    fun modify(dto: DTO, generator: DocxGenerator<DTO>)
}
