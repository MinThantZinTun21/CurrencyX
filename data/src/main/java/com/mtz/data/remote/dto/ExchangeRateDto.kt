package com.mtz.data.remote.dto

data class ExchangeRateDto(
    val source: String,
    val quote: List<QuoteDto>
)
data class QuoteDto(
    val source: String,
    val to: String,
    val rate: Double
)
