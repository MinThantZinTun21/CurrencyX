package com.mtz.domain.mapper

import com.mtz.currencyex.mapper.DomainLayerMapper
import com.mtz.data.remote.dto.ExchangeRateDto
import com.mtz.domain.model.ExchangeRateModel
import javax.inject.Inject

class ExchangeRateMapper @Inject constructor(
    private val quoteMapper: QuoteMapper
) :
    DomainLayerMapper<ExchangeRateModel, com.mtz.data.remote.dto.ExchangeRateDto> {
    override fun toDomain(data: ExchangeRateDto): ExchangeRateModel {
        return ExchangeRateModel(
            source = data.source,
            quotes = quoteMapper.toDomain(data.quote)
        )
    }

    override fun toDomain(data: List<ExchangeRateDto>): List<ExchangeRateModel> {
        return data.map {
            ExchangeRateModel(
                source = it.source,
                quotes = quoteMapper.toDomain(it.quote)
            )
        }
    }

    override fun toDto(domain: ExchangeRateModel): ExchangeRateDto {
        return ExchangeRateDto(
            source = domain.source,
            quote = quoteMapper.toDto(domain.quotes)
        )
    }

    override fun toDto(domain: List<ExchangeRateModel>): List<ExchangeRateDto> {
        return domain.map {
            ExchangeRateDto(
                source = it.source,
                quote = quoteMapper.toDto(it.quotes)
            )
        }
    }
}