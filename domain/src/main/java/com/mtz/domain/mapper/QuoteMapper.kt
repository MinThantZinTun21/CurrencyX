package com.mtz.domain.mapper

import com.mtz.currencyex.mapper.DomainLayerMapper
import com.mtz.data.remote.dto.QuoteDto
import com.mtz.domain.model.QuoteModel

class QuoteMapper : DomainLayerMapper<QuoteModel, QuoteDto> {
    override fun toDomain(data: QuoteDto): QuoteModel {
        return QuoteModel(
            source = data.source,
            to = data.to,
            rate = data.rate
        )
    }

    override fun toDomain(data: List<QuoteDto>): List<QuoteModel> {
        return data.map {
            QuoteModel(
                source = it.source,
                to = it.to,
                rate = it.rate
            )
        }
    }

    override fun toDto(domain: QuoteModel): QuoteDto {
        return QuoteDto(
            source = domain.source,
            to = domain.to,
            rate = domain.rate
        )
    }

    override fun toDto(domain: List<QuoteModel>): List<QuoteDto> {
        return domain.map {
            QuoteDto(
                source = it.source,
                to = it.to,
                rate = it.rate
            )
        }
    }
}