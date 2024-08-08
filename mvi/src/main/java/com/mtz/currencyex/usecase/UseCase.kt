package com.mtz.currencyex.usecase

import com.mtz.currencyex.fn.Either
import com.mtz.currencyex.fn.Failure
import com.mtz.currencyex.fn.RESULT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


abstract class UseCase<in Parameter, result >(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    abstract fun execute(parameters: Parameter): Flow<result>
}
