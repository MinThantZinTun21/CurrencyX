package com.mtz.domain.usecases

import com.mtz.currencyex.fn.Either
import com.mtz.currencyex.fn.Failure
import com.mtz.currencyex.fn.RESULT
import com.mtz.currencyex.usecase.UseCase
import com.mtz.data.remote.repository.ExchageRateRepositoryImpl
import com.mtz.domain.IoDispatcher
import com.mtz.domain.mapper.ExchangeRateMapper
import com.mtz.domain.model.ExchangeRateModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val exchangeRateRepository: ExchageRateRepositoryImpl,
    private val exchangeRateMapper: ExchangeRateMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<String, RESULT<ExchangeRateModel>>(dispatcher) {
    override fun execute(parameters: String): Flow<RESULT<ExchangeRateModel>> {
        return flow<RESULT<ExchangeRateModel>> {
            val result = exchangeRateRepository.getExchangeRate(parameters)
            if (result is Either.SUCCESS) {
                emit(Either.SUCCESS(exchangeRateMapper.toDomain(result.DATA)))
            } else {
                val error = result as Either.FAILED
                emit(Either.FAILED(error.ERROR))
            }
        }.flowOn(dispatcher).catch { e ->
            emit(Either.FAILED(Failure.Network(Failure.Domain(e.message).errorMessage)))
        }
    }


}