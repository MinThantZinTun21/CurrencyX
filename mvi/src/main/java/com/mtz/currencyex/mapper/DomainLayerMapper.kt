package com.mtz.currencyex.mapper

interface DomainLayerMapper<domain, dto> {
    fun toDomain(data: dto): domain
    fun toDto(domain: domain): dto
    fun toDomain(data: List<dto>): List<domain>
    fun toDto(domain: List<domain>): List<dto>
}