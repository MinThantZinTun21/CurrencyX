package com.mtz.data.remote.repository

import com.mtz.currencyex.fn.RESULT
import com.mtz.data.remote.dto.ExchangeRateDto


interface ExchangeRateRepository {
    suspend fun getExchangeRate(source: String): RESULT<ExchangeRateDto>

}