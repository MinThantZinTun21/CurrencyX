package com.mtz.data.remote.repository

import com.mtz.currencyex.fn.Either
import com.mtz.currencyex.fn.Failure
import com.mtz.currencyex.fn.RESULT
import com.mtz.currencyex.repository.BaseRepository
import com.mtz.data.remote.ApiService
import com.mtz.data.remote.dto.ExchangeRateDto
import com.mtz.data.remote.dto.QuoteDto
import okhttp3.internal.toImmutableList
import javax.inject.Inject

class ExchageRateRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ExchangeRateRepository, BaseRepository {
    override suspend fun getExchangeRate(source: String): RESULT<ExchangeRateDto> {
        return try {
            //todo api key should be in config skip for now
            val result = apiService.getExchangeRates(source)
            if (result.success) {
                // TODO: use mapper for more readable code
                //remove hardcoded source USD on ApiKey is valid
                //replace with source from api for remove prefix
                val quoteList = result.quotes?.entries?.map {
                    QuoteDto(
                        source = source,
                        to = it.key.removePrefix("USD"),
                        rate = it.value
                    )
                }?.toMutableList() ?: mutableListOf()
                //for include it self api just for display in list
                quoteList.add(QuoteDto(source = source, to = "USD", rate = 1.0))
                Either.SUCCESS(
                    ExchangeRateDto(
                        source,
                        quoteList
                    )
                )


            } else {
                Either.FAILED(Failure.ThirdParty(result.error?.info.toString()))

            }
        } catch (e: Throwable) {
            Either.FAILED(handleException(e))
        }

    }
}