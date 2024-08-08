package com.mtz.domain.model


data class ExchangeRateModel(
    val source: String,
    val quotes: List<QuoteModel>,
)

data class QuoteModel(
    val source: String,
    val to: String,
    val rate: Double
)